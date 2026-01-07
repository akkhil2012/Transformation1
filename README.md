# Transformation Platform (V2 ➜ V3)

This repository contains a sample Spring Boot architecture that keeps your current V2 REST API live, persists V2 data into `schemav2`, emits an outbox event, and then transforms that payload into V3 before writing into `schemav3`.

## Services

| Service | Port | Responsibility |
| --- | --- | --- |
| `v2-service` | 8081 | V2 customer API, writes to `schemav2` tables + `outbox_table1` + `transformation_table`. |
| `transformer-service` | 8083 | External transformation service that converts V2 payloads into V3 payloads. |
| `outbox-poller` | 8084 | Polls the outbox + transformation flag, calls transformer and V3 APIs. |
| `v3-service` | 8082 | V3 customer API, writes to `schemav3` tables. |

## Architecture diagram

```mermaid
flowchart LR
    Client[Legacy Clients]
    External[External Systems]

    subgraph V2[Spring REST V2]
        V2Api["/api/v2/customers"]
        V2Db[(schemav2)]
        Outbox[(outbox_table1)]
        TransformFlag[(transformation_table)]
        V2Api --> V2Db
        V2Api --> Outbox
        V2Api --> TransformFlag
    end

    subgraph Transform[Transformation Adapter]
        Transformer["transformer-service"]
    end

    subgraph Poller[Outbox Poller]
        PollerSvc["outbox-poller"]
    end

    subgraph V3[Spring REST V3]
        V3Api["/api/v3/customers"]
        V3Db[(schemav3)]
        V3Api --> V3Db
    end

    Client --> V2Api
    External --> V3Api
    PollerSvc --> Outbox
    PollerSvc --> TransformFlag
    PollerSvc --> Transformer
    PollerSvc --> V3Api
```

## Flow diagram

```mermaid
sequenceDiagram
    participant Client as Existing Customer
    participant V2 as v2-service
    participant V2DB as schemav2
    participant Outbox as outbox_table1
    participant Flag as transformation_table
    participant Poller as outbox-poller
    participant Transform as transformer-service
    participant V3 as v3-service
    participant V3DB as schemav3

    Client->>V2: POST /api/v2/customers (V2 payload)
    V2->>V2DB: write customers + orders
    V2->>Outbox: write event payload
    V2->>Flag: write status=PENDING

    Poller->>Flag: poll PENDING
    Poller->>Outbox: load payload
    Poller->>Transform: POST /api/transform/v2-to-v3
    Transform-->>Poller: V3 payload
    Poller->>V3: POST /api/v3/customers
    V3->>V3DB: write customers + orders + profiles
    Poller->>Flag: update status=COMPLETED
```

## Schema mapping

- **schemav2**
  - `customers`
  - `orders`
  - `outbox_table1`
  - `transformation_table`
- **schemav3**
  - `customers`
  - `orders`
  - `customer_profiles` (extra table in V3)

## Quick start

```bash
mvn -pl services/v2-service spring-boot:run
mvn -pl services/transformer-service spring-boot:run
mvn -pl services/v3-service spring-boot:run
mvn -pl services/outbox-poller spring-boot:run
```

## Example payloads

### V2 request

```json
{
  "externalId": "CUST-1001",
  "name": "Ada Lovelace",
  "email": "ada@example.com",
  "orders": [
    { "orderNumber": "ORD-1", "total": 120.50 }
  ]
}
```

### V3 request

```json
{
  "externalId": "CUST-1001",
  "fullName": "Ada Lovelace",
  "email": "ada@example.com",
  "addressLine1": "",
  "city": "",
  "orders": [
    { "orderNumber": "ORD-1", "total": 120.50 }
  ]
}
```
