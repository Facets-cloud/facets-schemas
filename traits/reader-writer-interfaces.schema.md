## Properties

| Property | Type              | Required | Description        |
|----------|-------------------|----------|--------------------|
| `writer` | [object](#writer) | **Yes**  | The writer details |
| `reader` | [object](#reader) | No       | The reader details |

## reader

The reader details

### Properties

| Property            | Type   | Required | Description                    |
|---------------------|--------|----------|--------------------------------|
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |

## writer

The writer details

### Properties

| Property            | Type   | Required | Description                    |
|---------------------|--------|----------|--------------------------------|
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |

