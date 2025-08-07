Feature: Product Management
  As an API user, I want to create and manage products
  to keep the inventory up to date.

  Scenario: Successfully creating a new product
    Given API is ready to receive new products
    When I send a POST request to "/v1/api/products" with the following body:
      | name          | manufacturer | amountInStock | price |
      | Laptop Pro X1 | TechCorp     | 50            | 1200  |
    Then the response status code must be 201
    And the response body must be "Product Successfully Created"

  Scenario: Attempting to create a product with a name that already exists
    Given a product exists with the name "Laptop Pro X1"
    When I send a POST request to "/v1/api/products" with the following body:
      | name          | manufacturer | amountInStock | price |
      | Laptop Pro X1 | OtherBrand   | 10            | 1150  |
    Then the response status code must be 400
    And the response must contain the error message "Product name is already exists"

  Scenario: Attempting to create a product with invalid data (blank name)
    Given API is ready to receive new products
    When I send a POST request to "/v1/api/products" with the following body:
      | name | manufacturer | amountInStock | price |
      |      | TechCorp     | 30            | 999   |
    Then the response status code must be 400
    And the response must contain the validation error "Name can not be empty or null."

    Scenario: Attempting to get all products
      Given API has products on database
      When I send a GET request to "/v1/api/products"
      Then the response status code must be 200
      And the response must be list type

    Scenario: Attempting to get one product when is registered:
      Given a product with the following details exists in the database:
        | id | name      | manufacturer | amountInStock | price |
        | 1  | Product 1 | Manufacturer | 30            | 10.00 |
      When I send a GET request to "/v1/api/products/1"
      Then the response status code must be 200
      And the response body should contain the following product details:
        | field         | value        |
        | name          | Product 1    |
        | manufacturer  | Manufacturer |
        | price         | 10.0         |
        | amountInStock | 30           |

