# Iam Policy

## Schema Properties

|                      | Type    | Description   | Required   |
|:---------------------|:--------|:--------------|:-----------|
| $SCHEMA_NAME         | string  |               | No         |
| kind                 | string  |               | Yes        |
| flavor               | string  |               | Yes        |
| lifecycle            | string  |               | Yes        |
| disabled             | boolean |               | Yes        |
| provided             | boolean |               | Yes        |
| depends_on           | array   |               | Yes        |
| metadata.annotations | object  |               | No         |

## Outputs

| Type   | Description   | Required   | Referencing   |
|--------|---------------|------------|---------------|

