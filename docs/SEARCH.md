# Search API

<br>

## Searching transactions

Request:

    POST /api/_search

    {
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

Response:

    {
        ...
    }
