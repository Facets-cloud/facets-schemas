## Introduction
Schema for table object of schemahero


## Spec


| Property       | Type   | Required | Description                         |
|----------------|--------|----------|-------------------------------------|
| `database`     | string | **Yes**  | Name of the database object         |
| `connection`   | string | **Yes**  | Database connection type            |
| `primary_key`  | string | **Yes**  | Primary Key of the table            |
| `columns`      | map    | **Yes**  | Columns of the table                |
| `indexes`      | map    | **No**   | Indexes of the table                |
| `foreign_keys` | map    | **No**   | Foreign keys reference of the table |


## Advanced
| Name                       | Description                                                                 | Datatype | Required |
|----------------------------|-----------------------------------------------------------------------------|----------|----------|
| default.seedData           | Seed data to be added to the table.                                         | map      | no       |

## Flavors

- default