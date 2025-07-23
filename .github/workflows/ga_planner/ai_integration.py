"""
AI Integration Module

This module provides functionality to interact with OpenAI API for generating
summary reports from terraform plan analysis.
"""

import json
import os
from datetime import datetime
from typing import Dict, List, Any, Optional
import openai
from openai import OpenAI
import tiktoken


class OpenAIAnalyzer:
    """
    A class to interact with OpenAI for terraform plan analysis and summary generation.
    """
    
    def __init__(self, api_key: Optional[str] = None):
        """
        Initialize the OpenAI analyzer.
        
        Args:
            api_key (Optional[str]): OpenAI API key. If not provided, will use OPENAI_API_KEY env var.
        """
        self.api_key = api_key or os.getenv("OPENAI_API_KEY")
        if not self.api_key:
            raise ValueError("OpenAI API key is required. Set OPENAI_API_KEY environment variable.")
        
        self.client = OpenAI(api_key=self.api_key)
        self.model = "gpt-4o-mini"  # Using GPT-4o-mini for cost efficiency
        
        # Initialize tokenizer for accurate token counting
        try:
            self.tokenizer = tiktoken.encoding_for_model(self.model)
        except KeyError:
            # Fallback to gpt-4 tokenizer if model-specific one not found
            self.tokenizer = tiktoken.encoding_for_model("gpt-4")
    
    def _create_analysis_prompt(self, plan_data: Dict[str, Any], 
                               previous_plan_data: Dict[str, Any],
                               additional_context: str = "") -> str:
        """
        Creates a comprehensive prompt for OpenAI to analyze terraform plans.
        
        Args:
            plan_data (Dict[str, Any]): The filtered terraform plan data
            additional_context (str): Additional context to include in the prompt
        
        Returns:
            str: The formatted prompt for Claude
        """
        prompt = f"""Please analyze the following terraform plan data and provide results in the specific tabular format described below.

{additional_context}

## Terraform Plan Data:

### Current RC Plan Data:
```json
{json.dumps(plan_data)}
```

### Previous RC Plan Data:
```json
{json.dumps(previous_plan_data)}
```

## Required Analysis Format:

You must analyze the plan data and organize the results into the following tables in markdown format:

### 1. Resources to be Destroyed
Create a table with columns: Resource Address | Severity
- **Severity Levels**: High (infrastructure, networks, clusters, services), Medium (databases, storage), Low (addons, configs)
- Include resources with "delete" action AND resources being replaced (create+delete actions)

### 1. Resources to be Replaced
Create a table with columns: Resource Address | Severity | Attributes forcing replacement (replace_paths)
- **Severity Levels**: High (infrastructure, networks, clusters, services), Medium (databases, storage), Low (addons, configs)
- Only include resources being replaced (create+delete actions)

### 2. Resources to be Updated  
Create a table with columns: Resource Address | Severity | Resource Attributes Changes
- **Severity Levels**: High (services, infrastructure, databases), Medium (networking, security), Low (addons, configs)
- Only include resources with "update" action
- List the specific attributes being changed

### 3. Resources to be Created
Create a table with columns: Resource Address
- Only include resources with "create" action (not replace)

### 4. Carried Forward Changes from Previous RC Plan
Create a table with columns: Resource Address | Action Type | Severity
- Compare current plan with previous RC plan data (if available in the context)
- Include resources that have the same action in both current and previous RC plans
- **Priority Rule**: If a resource appears in this table, exclude it from tables 1-3
- Action types: create, update, delete, replace

## Severity Calculation Guidelines:

**High Severity:**
- Infrastructure resources (VPC, subnets, clusters)
- Kubernetes clusters and services
- Databases and data stores
- Load balancers and networking components
- Security groups and IAM roles

**Medium Severity:**
- Storage volumes and configurations
- Networking rules and policies
- Monitoring and logging components
- Backup configurations

**Low Severity:**
- Add-on configurations
- Environment variables
- Tags and labels
- Documentation resources

## Output Format:
- Present each table in markdown format, and format it.
- Use "No changes" if any table is empty
- Focus on the tabular data as the primary deliverable

Please analyze the provided plan data and generate the tables according to these specifications."""
        
        # Count and log tokens for this prompt
        token_count = len(self.tokenizer.encode(prompt))
        print(f"OpenAI Analysis Prompt - Token count: {token_count}")
        
        return prompt
    
    async def analyze_single_plan(self, plan_data: Dict[str, Any], 
                                 previous_plan_data: Dict[str, Any],
                                 environment_context: str = "") -> Optional[str]:
        """
        Analyzes a single terraform plan using OpenAI.
        
        Args:
            plan_data (Dict[str, Any]): The terraform plan data to analyze
            environment_context (str): Additional context about the environment
        
        Returns:
            Optional[str]: The analysis report from OpenAI or None if error
        """
        try:
            prompt = self._create_analysis_prompt(plan_data, previous_plan_data, environment_context)
            
            response = self.client.chat.completions.create(
                model=self.model,
                messages=[
                    {"role": "system", "content": "You are a DevOps expert analyzing Terraform plans. Provide detailed, actionable insights."},
                    {"role": "user", "content": prompt}
                ],
                max_tokens=4000,
                temperature=0.1
            )
            
            return response.choices[0].message.content
            
        except Exception as e:
            print(f"Error analyzing plan with OpenAI: {e}")
            return None
    
    def generate_change_summary(self, resource_changes: List[Dict[str, Any]], 
                               environment_name: str = "Unknown") -> Optional[str]:
        """
        Generates a focused summary of resource changes only.
        
        Args:
            resource_changes (List[Dict[str, Any]]): List of resource changes
            environment_name (str): Name of the environment
        
        Returns:
            Optional[str]: A concise summary of changes
        """
        try:
            if not resource_changes:
                return f"No resource changes detected in {environment_name}."
            
            # Count changes by type
            changes_count = {"create": 0, "update": 0, "delete": 0, "replace": 0}
            critical_resources = []
            
            for change in resource_changes:
                actions = change.get("change", {}).get("actions", [])
                resource_type = change.get("type", "")
                resource_name = change.get("name", "")
                
                if "create" in actions and "delete" in actions:
                    changes_count["replace"] += 1
                    # Flag critical resources being replaced
                    if any(critical in resource_type.lower() for critical in 
                          ["database", "cluster", "security_group", "load_balancer"]):
                        critical_resources.append(f"{resource_type}.{resource_name}")
                elif "create" in actions:
                    changes_count["create"] += 1
                elif "update" in actions:
                    changes_count["update"] += 1
                elif "delete" in actions:
                    changes_count["delete"] += 1
                    # Flag critical resources being deleted
                    if any(critical in resource_type.lower() for critical in 
                          ["database", "cluster", "security_group", "load_balancer"]):
                        critical_resources.append(f"{resource_type}.{resource_name}")
            
            # Generate summary
            summary = f"""## {environment_name} - Resource Changes Summary

**Total Changes:** {sum(changes_count.values())}
- 🆕 **Create:** {changes_count['create']} resources
- 📝 **Update:** {changes_count['update']} resources  
- 🗑️ **Delete:** {changes_count['delete']} resources
- 🔄 **Replace:** {changes_count['replace']} resources

"""
            
            if critical_resources:
                summary += f"""
**⚠️ Critical Resources Affected:**
{chr(10).join(f'- {resource}' for resource in critical_resources)}
"""
            
            # Risk assessment
            total_changes = sum(changes_count.values())
            risk_level = "Low"
            if total_changes > 50 or changes_count["delete"] > 5 or critical_resources:
                risk_level = "High"
            elif total_changes > 20 or changes_count["delete"] > 0:
                risk_level = "Medium"
            
            summary += f"""
**Risk Level:** {risk_level}
"""
            
            return summary
            
        except Exception as e:
            print(f"Error generating change summary: {e}")
            return None
    
    def save_analysis_report(self, analysis: str, file_path: str) -> bool:
        """
        Saves the analysis report to a file with metadata.
        
        Args:
            analysis (str): The analysis report content
            file_path (str): Path where to save the report
        
        Returns:
            bool: True if saved successfully, False otherwise
        """
        try:
            report_content = f"""# Terraform Plan Analysis Report

**Generated:** {datetime.now().isoformat()}

"""
            report_content += analysis

            # Ensure directory exists
            os.makedirs(os.path.dirname(file_path), exist_ok=True)

            with open(file_path, 'w') as f:
                f.write(report_content)

            return True

        except Exception as e:
            print(f"Error saving analysis report to {file_path}: {e}")
            return False


# Convenience functions for easy integration
async def analyze_terraform_plan_file(file_path: str,
                                    previous_plan_path: str, 
                                    api_key: Optional[str] = None,
                                    environment_name: str = "") -> Optional[str]:
    """
    Convenience function to analyze a terraform plan file directly.
    
    Args:
        file_path (str): Path to the terraform plan JSON file
        api_key (Optional[str]): OpenAI API key
        environment_name (str): Name of the environment for context
    
    Returns:
        Optional[str]: Analysis report or None if error
    """
    try:
        with open(file_path, 'r') as f:
            plan_data = json.load(f)
        
        with open(previous_plan_path, 'r') as f:
            previous_plan_data = json.load(f)
        
        analyzer = OpenAIAnalyzer(api_key)
        context = f"Environment: {environment_name}" if environment_name else ""
        
        return await analyzer.analyze_single_plan(plan_data, previous_plan_data, context)
        
    except Exception as e:
        print(f"Error analyzing plan file {file_path}: {e}")
        return None

