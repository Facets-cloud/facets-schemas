"""
Terraform Plan Filtering Module

This module provides functionality to filter sensitive attributes from terraform plans
and focus analysis on resource_changes only.
"""

import json
import re
import yaml
from typing import Dict, List, Any, Optional, Set


class TerraformPlanFilter:
    """
    A class to filter sensitive attributes from terraform plans and process resource changes.
    Uses only Terraform's native sensitive markings from before_sensitive and after_sensitive.
    """
    
    def __init__(self):
        self.sensitive_paths = set()  # Store exact JSON paths that are sensitive
        
        # Maintainable list of resource types/names to exclude from analysis
        self.excluded_resources = [
            "scratch_string.release_metadata",
            "null_resource.qa_deployment",
            "null_resource.qa_deployment_new",
            "null_resource.test_dr"
            # Add more exclusions here as needed
            # "resource_type.resource_name",
        ]
    
    def _should_exclude_resource(self, change: Dict[str, Any]) -> bool:
        """
        Checks if a resource should be excluded from analysis based on the exclusion list.
        
        Args:
            change (Dict[str, Any]): A resource change object
        
        Returns:
            bool: True if the resource should be excluded
        """
        if not isinstance(change, dict):
            return False
        
        resource_type = change.get("type", "")
        resource_name = change.get("name", "")
        
        # Create the resource identifier in format: resource_type.resource_name
        resource_identifier = f"{resource_type}.{resource_name}"
        
        # Check against exclusion list
        return resource_identifier in self.excluded_resources
    
    def _detect_sensitive_attributes_from_plan(self, plan_data: Dict[str, Any]) -> None:
        """
        Auto-detects sensitive paths from terraform plan's before_sensitive and after_sensitive markings.
        Only uses Terraform's native sensitive markings.
        
        Args:
            plan_data (Dict[str, Any]): The terraform plan data
        """
        resource_changes = plan_data.get("resource_changes", [])
        
        for i, change in enumerate(resource_changes):
            if not isinstance(change, dict):
                continue
                
            change_obj = change.get("change", {})
            if not isinstance(change_obj, dict):
                continue
            
            # Process before_sensitive markings
            before_sensitive = change_obj.get("before_sensitive", {})
            if isinstance(before_sensitive, dict):
                self._extract_sensitive_paths_from_sensitive_map(
                    before_sensitive, f"resource_changes[{i}].change.before"
                )
            
            # Process after_sensitive markings
            after_sensitive = change_obj.get("after_sensitive", {})
            if isinstance(after_sensitive, dict):
                self._extract_sensitive_paths_from_sensitive_map(
                    after_sensitive, f"resource_changes[{i}].change.after"
                )
    
    def _extract_sensitive_paths_from_sensitive_map(self, sensitive_map: Dict[str, Any], base_path: str) -> None:
        """
        Extracts exact JSON paths from Terraform's sensitive map (before_sensitive/after_sensitive).
        
        Args:
            sensitive_map (Dict[str, Any]): The sensitive map from terraform (before_sensitive/after_sensitive)
            base_path (str): The base path for the current object (e.g., "resource_changes[0].change.before")
        """
        def traverse_sensitive_map(obj: Any, current_path: str) -> None:
            if isinstance(obj, dict):
                for key, value in obj.items():
                    path = f"{current_path}.{key}"
                    if value is True:
                        # This is a sensitive field, store the exact path
                        self.sensitive_paths.add(path)
                    elif isinstance(value, (dict, list)):
                        traverse_sensitive_map(value, path)
            elif isinstance(obj, list):
                for i, item in enumerate(obj):
                    path = f"{current_path}[{i}]"
                    if item is True:
                        self.sensitive_paths.add(path)
                    elif isinstance(item, (dict, list)):
                        traverse_sensitive_map(item, path)
        
        traverse_sensitive_map(sensitive_map, base_path)
    
    def _is_path_sensitive(self, current_path: str) -> bool:
        """
        Checks if a specific JSON path is marked as sensitive by Terraform.
        
        Args:
            current_path (str): The current JSON path being checked
        
        Returns:
            bool: True if this exact path is marked as sensitive
        """
        return current_path in self.sensitive_paths
    
    def filter_sensitive_data(self, data: Any, path: str = "") -> Any:
        """
        Recursively filters sensitive data using exact path matching from Terraform's sensitive markings.
        
        Args:
            data (Any): The data structure to filter
            path (str): The current JSON path in the data structure
        
        Returns:
            Any: The filtered data structure with sensitive values masked
        """
        if isinstance(data, dict):
            filtered_dict = {}
            for key, value in data.items():
                current_path = f"{path}.{key}" if path else key
                
                # Check if this exact path is marked as sensitive by Terraform
                if self._is_path_sensitive(current_path):
                    filtered_dict[key] = "[SENSITIVE_DATA_FILTERED]"
                else:
                    filtered_dict[key] = self.filter_sensitive_data(value, current_path)
            
            return filtered_dict
        
        elif isinstance(data, list):
            return [self.filter_sensitive_data(item, f"{path}[{i}]") 
                   for i, item in enumerate(data)]
        
        else:
            # For primitive values, only filter if the path is marked as sensitive
            if self._is_path_sensitive(path):
                return "[SENSITIVE_DATA_FILTERED]"
            return data
    
    def extract_resource_changes_only(self, plan_data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Extracts only the resource_changes section from a terraform plan.
        Also filters out no-op changes to focus on meaningful changes.
        
        Args:
            plan_data (Dict[str, Any]): The full terraform plan data
        
        Returns:
            Dict[str, Any]: A dictionary containing only meaningful resource_changes and metadata
        """
        if not isinstance(plan_data, dict):
            return {}
        
        resource_changes = plan_data.get("resource_changes", [])
        
        # Filter out no-op changes and excluded resources
        meaningful_changes = []
        no_op_count = 0
        excluded_count = 0
        
        for change in resource_changes:
            if self._should_exclude_resource(change):
                excluded_count += 1
            elif self._is_meaningful_change(change):
                meaningful_changes.append(change)
            else:
                no_op_count += 1
        
        # Create a simplified structure focusing on meaningful resource changes
        filtered_plan = {
            "terraform_version": plan_data.get("terraform_version", "unknown"),
            "format_version": plan_data.get("format_version", "unknown"),
            "resource_changes": meaningful_changes,
            "total_changes": len(meaningful_changes)
        }
        
        return filtered_plan
    
    def _is_meaningful_change(self, change: Dict[str, Any]) -> bool:
        """
        Determines if a resource change is meaningful (not a no-op).
        
        Args:
            change (Dict[str, Any]): A resource change object
        
        Returns:
            bool: True if the change is meaningful, False if it's a no-op
        """
        if not isinstance(change, dict):
            return False
        
        change_obj = change.get("change", {})
        if not isinstance(change_obj, dict):
            return False
        
        actions = change_obj.get("actions", [])
        
        # Filter out no-op changes
        if not actions or actions == ["no-op"]:
            return False
        
        # Check for read-only operations that don't change infrastructure
        if actions == ["read"]:
            return False
        
        # Check for changes that are effectively no-ops
        before = change_obj.get("before")
        after = change_obj.get("after")
        
        # If both before and after are the same, it's likely a no-op
        if before is not None and after is not None:
            if self._are_values_equivalent(before, after):
                return False
        
        # Check for replacement with identical values
        if "create" in actions and "delete" in actions:
            if before is not None and after is not None:
                if self._are_values_equivalent(before, after):
                    return False
        
        return True
    
    def _are_values_equivalent(self, val1: Any, val2: Any) -> bool:
        """
        Checks if two values are equivalent for the purpose of change detection.
        
        Args:
            val1 (Any): First value
            val2 (Any): Second value
        
        Returns:
            bool: True if values are equivalent
        """
        # Handle None values
        if val1 is None and val2 is None:
            return True
        if val1 is None or val2 is None:
            return False
        
        # Handle different types
        if type(val1) != type(val2):
            return False
        
        # Handle dictionaries
        if isinstance(val1, dict) and isinstance(val2, dict):
            return self._are_dicts_equivalent(val1, val2)
        
        # Handle lists
        if isinstance(val1, list) and isinstance(val2, list):
            return self._are_lists_equivalent(val1, val2)
        
        # Handle basic types
        return val1 == val2
    
    def _are_dicts_equivalent(self, dict1: Dict[str, Any], dict2: Dict[str, Any]) -> bool:
        """
        Checks if two dictionaries are equivalent, ignoring order and certain metadata.
        
        Args:
            dict1 (Dict[str, Any]): First dictionary
            dict2 (Dict[str, Any]): Second dictionary
        
        Returns:
            bool: True if dictionaries are equivalent
        """
        # Check if keys are the same
        if set(dict1.keys()) != set(dict2.keys()):
            return False
        
        # Check each key-value pair
        for key in dict1.keys():
            if not self._are_values_equivalent(dict1[key], dict2[key]):
                return False
        
        return True
    
    def _are_lists_equivalent(self, list1: List[Any], list2: List[Any]) -> bool:
        """
        Checks if two lists are equivalent.
        
        Args:
            list1 (List[Any]): First list
            list2 (List[Any]): Second list
        
        Returns:
            bool: True if lists are equivalent
        """
        if len(list1) != len(list2):
            return False
        
        for i in range(len(list1)):
            if not self._are_values_equivalent(list1[i], list2[i]):
                return False
        
        return True
    
    def extract_changes_only(self, resource_changes: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        """
        Extract only the necessary information for each resource change based on operation type:
        - Create/Delete operations: Only resource address and basic info
        - Replace operations: Resource address, replace paths, and basic info
        - Update operations: Only changed attributes in before/after blocks
        
        Args:
            resource_changes (List[Dict[str, Any]]): List of resource changes
        
        Returns:
            List[Dict[str, Any]]: Resource changes with only the necessary information
        """
        changes_only = []
        
        for change in resource_changes:
            if not isinstance(change, dict):
                continue
            
            change_obj = change.get("change", {})
            if not isinstance(change_obj, dict):
                continue
            
            before = change_obj.get("before", {})
            after = change_obj.get("after", {})
            actions = change_obj.get("actions", [])
            
            # Create a new change object with only essential information
            change_diff = {
                "address": change.get("address", "").replace("module.level2.", ""),
                "change": {
                    "actions": actions
                }
            }
            
            # Extract only the necessary information based on operation type
            if "create" in actions and "delete" in actions:
                # For replace operations, include replace paths and basic info only
                # Add replace paths if available
                replace_paths = change_obj.get("replace_paths", [])
                if replace_paths:
                    change_diff["change"]["replace_paths"] = replace_paths
                # No changed attributes needed for replace operations
                
            elif "update" in actions:
                # For update operations, show only changed attributes in before/after blocks
                if isinstance(before, dict) and isinstance(after, dict):
                    filtered_before, filtered_after = self._remove_identical_keys(before, after)
                    if filtered_before or filtered_after:
                        change_diff["change"]["before"] = filtered_before
                        change_diff["change"]["after"] = filtered_after
                else:
                    # Fallback for non-dict values
                    if before != after:
                        change_diff["change"]["before"] = before
                        change_diff["change"]["after"] = after
            
            changes_only.append(change_diff)
        
        return changes_only
    
    def _extract_differences(self, before: Any, after: Any, path: str = "") -> tuple:
        """
        Recursively extract differences between before and after states.
        
        Args:
            before (Any): Before state
            after (Any): After state
            path (str): Current path in the object tree
        
        Returns:
            tuple: (before_changes, after_changes) containing only differing values
        """
        before_changes = {}
        after_changes = {}
        
        # Handle None values
        if before is None and after is None:
            return before_changes, after_changes
        if before is None:
            return before_changes, {"<NEW>": after}
        if after is None:
            return {"<DELETED>": before}, after_changes
        
        # Handle different types
        if type(before) != type(after):
            return {"<TYPE_CHANGED>": before}, {"<TYPE_CHANGED>": after}
        
        # Handle dictionaries
        if isinstance(before, dict) and isinstance(after, dict):
            all_keys = set(before.keys()) | set(after.keys())
            
            for key in all_keys:
                key_path = f"{path}.{key}" if path else key
                
                if key not in before:
                    after_changes[key] = {"<NEW>": after[key]}
                elif key not in after:
                    before_changes[key] = {"<DELETED>": before[key]}
                else:
                    nested_before, nested_after = self._extract_differences(
                        before[key], after[key], key_path
                    )
                    if nested_before:
                        before_changes[key] = nested_before
                    if nested_after:
                        after_changes[key] = nested_after
        
        # Handle lists
        elif isinstance(before, list) and isinstance(after, list):
            # For lists, we'll do a simple comparison
            # More sophisticated diff could be implemented if needed
            if len(before) != len(after):
                return {"<LIST_SIZE_CHANGED>": before}, {"<LIST_SIZE_CHANGED>": after}
            
            for i, (before_item, after_item) in enumerate(zip(before, after)):
                nested_before, nested_after = self._extract_differences(
                    before_item, after_item, f"{path}[{i}]"
                )
                if nested_before:
                    before_changes[f"[{i}]"] = nested_before
                if nested_after:
                    after_changes[f"[{i}]"] = nested_after
        
        # Handle primitive values
        else:
            if before != after:
                return {"<VALUE>": before}, {"<VALUE>": after}
        
        return before_changes, after_changes
    
    def _remove_identical_keys(self, before: dict, after: dict) -> tuple:
        """
        Remove identical key-value pairs between before and after dictionaries.
        Recursively handles nested dictionaries and lists to filter out identical elements.
        
        Args:
            before (dict): Before state dictionary
            after (dict): After state dictionary
        
        Returns:
            tuple: (filtered_before, filtered_after) containing only differing keys
        """
        filtered_before = {}
        filtered_after = {}
        
        # Loop through all keys in before
        for key, before_value in before.items():
            if key in after:
                after_value = after[key]
                # If values are different, handle based on type
                if before_value != after_value:
                    # Handle nested dictionaries
                    if isinstance(before_value, dict) and isinstance(after_value, dict):
                        nested_before, nested_after = self._remove_identical_keys(before_value, after_value)
                        if nested_before:
                            filtered_before[key] = nested_before
                        if nested_after:
                            filtered_after[key] = nested_after
                    # Handle lists - special case for Helm values
                    elif isinstance(before_value, list) and isinstance(after_value, list):
                        # Check if this is a Helm values list (YAML strings)
                        if key == "values" and self._is_helm_values_list(before_value, after_value):
                            filtered_before_list, filtered_after_list = self._process_helm_values_diff(before_value, after_value)
                            if filtered_before_list:
                                filtered_before[key] = filtered_before_list
                            if filtered_after_list:
                                filtered_after[key] = filtered_after_list
                        else:
                            # Regular list processing
                            filtered_before_list, filtered_after_list = self._remove_identical_list_elements(before_value, after_value)
                            if filtered_before_list:
                                filtered_before[key] = filtered_before_list
                            if filtered_after_list:
                                filtered_after[key] = filtered_after_list
                    # Handle primitive values
                    else:
                        filtered_before[key] = before_value
                        filtered_after[key] = after_value
                # If identical, skip both (remove from result)
            else:
                # Key doesn't exist in after, keep it in before
                filtered_before[key] = before_value
        
        # Loop through keys that are only in after (not in before)
        for key, after_value in after.items():
            if key not in before:
                filtered_after[key] = after_value
        
        return filtered_before, filtered_after

    def _remove_identical_list_elements(self, before_list: list, after_list: list) -> tuple:
        """
        Remove identical elements from two lists, keeping only the differences.
        
        Args:
            before_list (list): Before state list
            after_list (list): After state list
        
        Returns:
            tuple: (filtered_before_list, filtered_after_list) containing only differing elements
        """
        filtered_before = []
        filtered_after = []
        
        # Handle lists of different sizes
        max_length = max(len(before_list), len(after_list))
        
        for i in range(max_length):
            before_item = before_list[i] if i < len(before_list) else None
            after_item = after_list[i] if i < len(after_list) else None
            
            # If both items exist and are different
            if before_item is not None and after_item is not None:
                if before_item != after_item:
                    # Handle nested dictionaries in lists
                    if isinstance(before_item, dict) and isinstance(after_item, dict):
                        nested_before, nested_after = self._remove_identical_keys(before_item, after_item)
                        if nested_before:
                            filtered_before.append(nested_before)
                        if nested_after:
                            filtered_after.append(nested_after)
                    # Handle nested lists in lists
                    elif isinstance(before_item, list) and isinstance(after_item, list):
                        nested_before_list, nested_after_list = self._remove_identical_list_elements(before_item, after_item)
                        if nested_before_list:
                            filtered_before.append(nested_before_list)
                        if nested_after_list:
                            filtered_after.append(nested_after_list)
                    # Handle primitive values
                    else:
                        filtered_before.append(before_item)
                        filtered_after.append(after_item)
                # If items are identical, skip both
            elif before_item is not None:
                # Item only exists in before (removed)
                filtered_before.append(before_item)
            elif after_item is not None:
                # Item only exists in after (added)
                filtered_after.append(after_item)
        
        return filtered_before, filtered_after

    def _is_helm_values_list(self, before_list: list, after_list: list) -> bool:
        """
        Check if the given lists contain Helm values (YAML-encoded strings).
        
        Args:
            before_list (list): Before state list
            after_list (list): After state list
        
        Returns:
            bool: True if this appears to be a Helm values list
        """
        # Check if lists contain strings that look like YAML
        all_lists = [before_list, after_list]
        for lst in all_lists:
            if not lst:
                continue
            # Check if all items are strings and contain typical YAML patterns
            for item in lst:
                if not isinstance(item, str):
                    return False
                # Look for YAML-like patterns
                if not ((':' in item and '\n' in item) or 
                       item.strip().startswith(('---', '|', '>'))):
                    return False
        return True

    def _process_helm_values_diff(self, before_list: list, after_list: list) -> tuple:
        """
        Process Helm values lists to show only YAML differences.
        
        Args:
            before_list (list): Before state Helm values list
            after_list (list): After state Helm values list
        
        Returns:
            tuple: (filtered_before_list, filtered_after_list) containing only YAML differences
        """
        try:
            # Merge all YAML strings in each list
            before_yaml = self._merge_yaml_strings(before_list)
            after_yaml = self._merge_yaml_strings(after_list)
            
            # Parse YAML strings to dictionaries
            before_dict = yaml.safe_load(before_yaml) if before_yaml else {}
            after_dict = yaml.safe_load(after_yaml) if after_yaml else {}
            
            # Get only the differences
            if isinstance(before_dict, dict) and isinstance(after_dict, dict):
                filtered_before, filtered_after = self._remove_identical_keys(before_dict, after_dict)
                
                # Convert back to YAML strings if there are differences
                result_before = []
                result_after = []
                
                if filtered_before:
                    yaml_str = yaml.dump(filtered_before, default_flow_style=False, sort_keys=False)
                    result_before.append(f"# YAML differences (before):\n{yaml_str}")
                
                if filtered_after:
                    yaml_str = yaml.dump(filtered_after, default_flow_style=False, sort_keys=False)
                    result_after.append(f"# YAML differences (after):\n{yaml_str}")
                
                return result_before, result_after
            else:
                # Fallback to string comparison if YAML parsing fails
                return self._string_diff_fallback(before_list, after_list)
                
        except yaml.YAMLError as e:
            print(f"YAML parsing error in Helm values: {e}")
            # Fallback to regular list processing
            return self._remove_identical_list_elements(before_list, after_list)

    def _merge_yaml_strings(self, yaml_list: list) -> str:
        """
        Merge multiple YAML strings from a list into a single YAML string.
        
        Args:
            yaml_list (list): List of YAML strings
        
        Returns:
            str: Merged YAML string
        """
        if not yaml_list:
            return ""
        
        # Join all YAML strings with newlines
        merged = "\n".join(yaml_list)
        return merged

    def _string_diff_fallback(self, before_list: list, after_list: list) -> tuple:
        """
        Fallback method for string-based diff when YAML parsing fails.
        
        Args:
            before_list (list): Before state list
            after_list (list): After state list
        
        Returns:
            tuple: (filtered_before_list, filtered_after_list) containing only different strings
        """
        # Convert to sets for easier comparison
        before_set = set(before_list)
        after_set = set(after_list)
        
        # Find differences
        before_only = before_set - after_set
        after_only = after_set - before_set
        
        # Return only the differences
        filtered_before = list(before_only) if before_only else []
        filtered_after = list(after_only) if after_only else []
        
        return filtered_before, filtered_after

    def process_terraform_plan(self, plan_data: Dict[str, Any], 
                             filter_sensitive: bool = True,
                             resource_changes_only: bool = True,
                             changes_only: bool = False) -> Dict[str, Any]:
        """
        Main method to process a terraform plan with auto-detection and filtering.
        Processes in order: resource filtering -> auto-detection -> sensitive filtering -> changes extraction (last).
        
        Args:
            plan_data (Dict[str, Any]): The terraform plan data
            filter_sensitive (bool): Whether to filter sensitive attributes (configurable via env var)
            resource_changes_only (bool): Whether to extract only resource_changes
            changes_only (bool): Whether to extract only the differences between before/after states
        
        Returns:
            Dict[str, Any]: The processed terraform plan with optional sensitive data filtering and changes extraction
        """
        processed_plan = plan_data.copy()
        
        # Step 1: Apply structural filtering first (resource_changes extraction, no-op filtering, excluded resources)
        if resource_changes_only:
            processed_plan = self.extract_resource_changes_only(processed_plan)
        
        # Step 2: Run auto-detection on the filtered data to identify sensitive attributes
        self._detect_sensitive_attributes_from_plan(processed_plan)
        
        # Step 3: Apply sensitive data filtering (after detection but before changes extraction)
        if filter_sensitive:
            processed_plan = self.filter_sensitive_data(processed_plan)
        
        # Step 4: Extract only actual changes if requested (LAST step for maximum data reduction)
        if changes_only:
            resource_changes = processed_plan.get("resource_changes", [])
            changes_only_data = self.extract_changes_only(resource_changes)
            processed_plan["resource_changes"] = changes_only_data
            processed_plan["total_changes"] = len(changes_only_data)
        
        # No metadata block needed in filtered plan
        
        return processed_plan
    
    def get_change_summary(self, resource_changes: List[Dict[str, Any]]) -> Dict[str, Any]:
        """
        Generates a summary of changes from resource_changes.
        
        Args:
            resource_changes (List[Dict[str, Any]]): List of resource changes
        
        Returns:
            Dict[str, Any]: Summary of changes categorized by action type
        """
        summary = {
            "create": [],
            "update": [],
            "delete": [],
            "replace": [],
            "no_change": []
        }
        
        for change in resource_changes:
            if not isinstance(change, dict):
                continue
            
            actions = change.get("change", {}).get("actions", [])
            resource_type = change.get("type", "unknown")
            resource_name = change.get("name", "unknown")
            module_address = change.get("module_address", "root")
            
            resource_info = {
                "type": resource_type,
                "name": resource_name,
                "module_address": module_address,
                "actions": actions
            }
            
            if "create" in actions and "delete" in actions:
                summary["replace"].append(resource_info)
            elif "create" in actions:
                summary["create"].append(resource_info)
            elif "update" in actions:
                summary["update"].append(resource_info)
            elif "delete" in actions:
                summary["delete"].append(resource_info)
            else:
                summary["no_change"].append(resource_info)
        
        # Add counts
        summary["counts"] = {
            action: len(resources) for action, resources in summary.items() 
            if action != "counts"
        }
        
        return summary


def load_and_filter_plan(file_path: str, 
                        filter_sensitive: bool = True,
                        resource_changes_only: bool = True,
                        changes_only: bool = False) -> Optional[Dict[str, Any]]:
    """
    Convenience function to load and filter a terraform plan from a file.
    Always applies auto-detection, filtering is configurable.
    
    Args:
        file_path (str): Path to the terraform plan JSON file
        filter_sensitive (bool): Whether to filter sensitive attributes (configurable via env var)
        resource_changes_only (bool): Whether to extract only resource_changes
        changes_only (bool): Whether to extract only the differences between before/after states
    
    Returns:
        Optional[Dict[str, Any]]: The filtered plan data or None if error
    """
    try:
        with open(file_path, 'r') as f:
            plan_data = json.load(f)
        
        filter_instance = TerraformPlanFilter()
        return filter_instance.process_terraform_plan(
            plan_data, 
            filter_sensitive=filter_sensitive,
            resource_changes_only=resource_changes_only,
            changes_only=changes_only
        )
    
    except (FileNotFoundError, json.JSONDecodeError, Exception) as e:
        print(f"Error loading plan from {file_path}: {e}")
        return None