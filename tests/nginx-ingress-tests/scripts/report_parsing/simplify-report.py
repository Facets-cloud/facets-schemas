"""
Simplifies the combiend report.xml

Does the following:
- strips the golang package name to make the report more human readable
- aggregates time correctly for each test suite by adding time for each testcase
"""
import re
import sys
from typing import Any, Callable, List
import xml.etree.ElementTree as ET


def strip_package_path(root: ET.Element) -> None:
    """
    iterates over the xml tree and strips known attributes of golang package path
    """
    PREFIX = "github.com/Facets-cloud/facets-iac/tests/nginx-ingress-tests/"
    WHITELIST = {
        "testsuite": ["name"],
        "testcase": ["classname"],
    }
    for elem in root.iter():
        if elem.tag not in WHITELIST:
            continue
        for attr_to_strip in WHITELIST[elem.tag]:
            elem.attrib[attr_to_strip] = elem.attrib[attr_to_strip].removeprefix(PREFIX)
            __name = elem.attrib.get("name")
            print(
                f"Stripping {elem.tag}:{__name}".ljust(50),
                "Stripped value:",
                elem.attrib[attr_to_strip],
            )


def aggregate_test_timings(root: ET.Element) -> float:
    """
    jrm utility doesnt aggregate the test timing correctly because it assume
    those reports were generated in parallel, not sequentially.

    This method aggregates timings for each test suite.

    recursive method to traverse to all child elements and aggregate and update
    the timings

    using gotestsum's sample file as the golden one
    (https://github.com/gotestyourself/gotestsum/blob/main/internal/junitxml/testdata/junitxml-report-skip-empty.golden)
    we see `testcase` is the lowest tag containing a time, hence using that as base case

    how to proceed:
      when top element:
        - get aggregated timing of all child nodes
        - plus it all and update attribute timing value
      when lowest element:
        - return timing
    """
    # filter out non testcase/testsuite(s) tags
    if root.tag not in ["testcase", "testsuite", "testsuites"]:
        return 0
    print("\033[90m", "in ", root.tag, root.attrib.get("name"), "\033[0m")

    # base case
    if root.tag == "testcase":
        name = root.attrib["name"]

        ## check if test is not a subtest
        # we do a rudimentary check to detect a subtest if the testcase name
        # starts with a "Test" and contains a forward slash and some text
        # afterwards
        if re.findall(r"Test.+?/.+?", name):
            print(f"\tskipping subtest {name}")
            return 0

        print(f"\treturning testcase {name} timing {root.attrib['time']}")
        return float(root.attrib.get("time", "0"))

    aggregated_timing = 0
    for child_elem in root:
        aggregated_timing += aggregate_test_timings(child_elem)

    stored_time = float(root.attrib.get("time", 0.0))
    if stored_time != aggregated_timing:
        print(
            f"{root.tag}:{root.attrib.get('name')}: "
            f"DIFFERENCE in stored timing: {stored_time}, calculated timing: {aggregated_timing}"
        )

    # replace stored timing with calcaulated timing because there will always be
    # a difference in parent testsuite and sum of child testcases ranging from a
    # couple milliseconds to a few seconds
    root.attrib["time"] = str(aggregated_timing)
    return aggregated_timing


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(
            "expected arguments as following: python3 simplify-report.py <out-file> <in-file>",
        )
        exit(1)

    out_file, in_file = sys.argv[1:]

    report = ET.parse(in_file)
    # =========

    methods: List[Callable[[ET.Element], Any]] = [
        strip_package_path,
        aggregate_test_timings,
    ]

    root = report.getroot()
    for method in methods:
        print(("Executing " + method.__name__).center(80, "*"))
        method(root)

    report.write(out_file, encoding="unicode")
