# Filters API

<br>

## Creating a filter

**NOTE:** All fields are optional, although, at least one field must be specified.

**Request:**

    POST /api/filters

    {
        "from": "2016-01-01",
        "to": "2017-01-01",
        "name": "Albert Heijn filter",
        "description": "Albert Heijn",
        "amount": {
            "from": -1000,
            "to": 0
        },
        "accountHolder": "Account Holder",
        "account": "NL00 ABNA 0000 0000 00",
        "offsetAccount": "NL11 ABNA 1111 1111 11",
        "tags": [1, 2, ...],
        "options": [
            "INCOME",
            "EXPENSES",
            ...
        ]
    }

**Response:**

    201 Created

    {
        "id": 1,
        "createdAt": "2016-01-01 00:00:00",
        "name": "Albert Heijn filter",
        "from": "2016-01-01",
        "to": "2017-01-01",
        "description": "Albert Heijn",
        "amount": {
            "from": -1000,
            "to": 0
        },
        "accountHolder": "Account Holder",
        "account": "NL00 ABNA 0000 0000 00",
        "offsetAccount": "NL11 ABNA 1111 1111 11",
        "tags": [1, 2, ...],
        "options": [
            "INCOME",
            "EXPENSES",
            ...
        ]
    }

<br>

## Getting a filter

**NOTE:** Only non-empty fields are shown.

**Request:**

    GET /api/filters/1

**Response:**

    200 OK

    {
        "id": 1,
        "createdAt": "2016-01-01 00:00:00",
        "name": "Albert Heijn filter",
        "from": "2016-01-01",
        "to": "2017-01-01",
        "description": "Albert Heijn",
        "amount": {
            "from": -1000,
            "to": 0
        },
        "accountHolder": "Account Holder",
        "account": "NL00 ABNA 0000 0000 00",
        "offsetAccount": "NL11 ABNA 1111 1111 11",
        "tags": [1, 2, ...],
        "options": [
            "INCOME",
            "EXPENSES",
            ...
        ]
    }

**Error response:**

    404 Not Found

    {
        "error": "Filter [ID: 1] not found"
    }

<br>

## Listing filters

**NOTE:** Only non-empty fields are shown.

**Request:**

    GET /api/filters

**Response:**

    200 OK

    [
        {
            "id": 1,
            "createdAt": "2016-01-01 00:00:00",
            "name": "Albert Heijn filter",
            "from": "2016-01-01",
            "to": "2017-01-01",
            "description": "Albert Heijn",
            "amount": {
                "from": -1000,
                "to": 0
            },
            "accountHolder": "Account Holder",
            "account": "NL00 ABNA 0000 0000 00",
            "offsetAccount": "NL11 ABNA 1111 1111 11",
            "tags": [1, 2, ...],
            "options": [
                "INCOME",
                "EXPENSES",
                ...
            ]
        },
        ...
    ]

<br>

## Editing a filter

**NOTE:** All fields are optional, although, at least one field must be specified.

**Request:**

    PUT /api/filters/1

    {
        "from": "2016-01-01",
        "to": "2017-01-01",
        "name": "Albert Heijn filter",
        "description": "Albert Heijn",
        "amount": {
            "from": -1000,
            "to": 0
        },
        "accountHolder": "Account Holder",
        "account": "NL00 ABNA 0000 0000 00",
        "offsetAccount": "NL11 ABNA 1111 1111 11",
        "tags": [1, 2, ...],
        "options": [
            "INCOME",
            "EXPENSES",
            ...
        ]
    }

**Response:**

    200 OK

    {
        "id": 1,
        ...
    }

**'Not Modified' Response:**

    304 Not Modified

<br>

## Deleting a filter

**NOTE:** Filter won't be deleted if referenced in rules or reports.

**Request:**

    DELETE /api/filters/1

**Response:**

    204 No Content

**Error response:**

    403 Forbbiden

    {
        "error": "This filter is referenced in rules and/or reports"
    }
