## Peering Module Schema docs

## Properties

| Property   | Type            | Required | Description |
|------------|-----------------|----------|-------------|
| `spec`     | [object](#spec) | **Yes**  |             |
| `advanced` |                 | No       |             |
| `out`      | [object](#out)  | No       |             |

## out

### Properties

| Property     | Type                  | Required | Description                                                      |
|--------------|-----------------------|----------|------------------------------------------------------------------|
| `interfaces` | [object](#interfaces) | No       | The output for your peering module, can be generated or provided |

### interfaces

The output for your peering module, can be generated or provided

#### Properties

| Property            | Type   | Required | Description                    |
|---------------------|--------|----------|--------------------------------|
| `connection_string` | string | No       | Connection string to connect   |
| `host`              | string | No       | Host for service discovery     |
| `name`              | string | No       | Name of interface, same as key |
| `password`          | string | No       | Password to connect (if any)   |
| `port`              | string | No       | Port for service discovery     |
| `username`          | string | No       | Username to connect (if any)   |

## spec

### Properties

| Property     | Type   | Required | Description                                                |
|--------------|--------|----------|------------------------------------------------------------|
| `account_id` | string | **Yes**  | The account id of the cloud that you want to peer vpc with |
| `cidr`       | string | **Yes**  | The CIDR range of the vpc that you want to peered          |
| `region`     | string | **Yes**  | The region where the vpc exists in your account            |
| `vpc_id`     | string | **Yes**  | The accepter vpc id of the account id                      |

