[Окружение для теста.postman_environment.json](https://github.com/user-attachments/files/21535011/postman_environment.json)

{
	"info": {
		"_postman_id": "82d414f5-62d3-46c7-925a-cde3bcca5671",
		"name": "Тесты для проекта 1",
		"description": "Clients test#2 (fail with text in responce)\n\nClients test#3 (fail with text in responce)",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "37262306"
	},
	"item": [
		{
			"name": "Employees",
			"item": [
				{
					"name": "1. Create Employee - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 201 Created\", () => pm.response.to.have.status(201));",
									"pm.test(\"Response has ID\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json).to.have.property('id');",
									"    pm.environment.set('employeeId', json.id);",
									"});",
									"pm.test(\"Response contains all fields\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json.name).to.eql(\"Иван\");",
									"    pm.expect(json.surname).to.eql(\"Иванов\");",
									"    pm.expect(json.position).to.eql(\"MANAGER\");",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Иван\",\n  \"surname\": \"Иванов\",\n  \"emailAddress\": \"ivan@example.com\",\n  \"password\": \"secret123\",\n  \"position\": \"MANAGER\"\n}"
						},
						"url": {
							"raw": "{{URL_employees_create}}",
							"host": [
								"{{URL_employees_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "2. Create Employee - Invalid Position",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Позиция должна быть MANAGER или ADMINISTRATOR');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Петр\",\n  \"surname\": \"Петров\",\n  \"emailAddress\": \"petr@example.com\",\n  \"password\": \"secret123\",\n  \"position\": \"INVALID\"\n}"
						},
						"url": {
							"raw": "{{URL_employees_create}}",
							"host": [
								"{{URL_employees_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "3. Create Employee - Short Password",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Пароль должен быть не менее 6 символов');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Алексей\",\n  \"surname\": \"Алексеев\",\n  \"emailAddress\": \"alex@example.com\",\n  \"password\": \"short\",\n  \"position\": \"MANAGER\"\n}"
						},
						"url": {
							"raw": "{{URL_employees_create}}",
							"host": [
								"{{URL_employees_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "4. Create Employee - Duplicate Email",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 409 Conflict\", () => pm.response.to.have.status(409));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Email уже существует');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Дмитрий\",\n  \"surname\": \"Дмитриев\",\n  \"emailAddress\": \"ivan@example.com\",\n  \"password\": \"secret456\",\n  \"position\": \"MANAGER\"\n}"
						},
						"url": {
							"raw": "{{URL_employees_create}}",
							"host": [
								"{{URL_employees_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "5. Get Employee - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Correct employee returned\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json.id).to.eql(parseInt(pm.environment.get('employeeId')));",
									"    pm.expect(json.name).to.eql(\"Иван\");",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_employees_get}}/{{employeeId}}",
							"host": [
								"{{URL_employees_get}}"
							],
							"path": [
								"{{employeeId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "6. Get Employee - Not Found",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 404 Not Found\", () => pm.response.to.have.status(404));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Сотрудник не найден');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_employees_get}}/999999",
							"host": [
								"{{URL_employees_get}}"
							],
							"path": [
								"999999"
							]
						}
					},
					"response": []
				},
				{
					"name": "7. Update Employee - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Fields updated\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json.name).to.eql(\"Иван\");",
									"    pm.expect(json.surname).to.eql(\"Updated\");",
									"    pm.expect(json.phoneNumber).to.eql(\"+79001234567\");",
									"    pm.expect(json.email).to.eql(\"ivan.updated@example.com\");",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "PATCH",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Иван\",\n  \"surname\": \"Updated\",\n  \"email\": \"ivan.updated@example.com\"\n}"
						},
						"url": {
							"raw": "{{URL_employees_update}}{{employeeId}}",
							"host": [
								"{{URL_employees_update}}{{employeeId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "8. Update Employee - Invalid Email",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Некорректный формат email');",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						},
						{
							"listen": "prerequest",
							"script": {
								"packages": {},
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "PATCH",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"email\": \"invalid-email\"\n}"
						},
						"url": {
							"raw": "{{URL_employees_update}}{{employeeId}}",
							"host": [
								"{{URL_employees_update}}{{employeeId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "9. Update Employee - Duplicate Email",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 409 Conflict\", () => pm.response.to.have.status(409));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Email уже существует');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "PATCH",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"email\": \"ivan@example.com\"\n}"
						},
						"url": {
							"raw": "{{URL_employees_update}}{{employeeId}}",
							"host": [
								"{{URL_employees_update}}{{employeeId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "10. Get All Employees - No Filter",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Returns paginated response\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json).to.have.property('content');",
									"    pm.expect(json).to.have.property('totalElements');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_employees_get}}",
							"host": [
								"{{URL_employees_get}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "11. Get All Employees - Filter by Position",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Only MANAGERs returned\", () => {",
									"    const json = pm.response.json();",
									"    json.content.forEach(employee => {",
									"        pm.expect(employee.position).to.eql(\"MANAGER\");",
									"    });",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_employees_get}}?position=MANAGER",
							"host": [
								"{{URL_employees_get}}"
							],
							"query": [
								{
									"key": "position",
									"value": "MANAGER"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "12. Get All Employees - Filter by Name",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Name filter works\", () => {",
									"    const json = pm.response.json();",
									"    json.content.forEach(employee => {",
									"        pm.expect(employee.name).to.include(\"Иван\");",
									"    });",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_employees_get}}?name=Иван",
							"host": [
								"{{URL_employees_get}}"
							],
							"query": [
								{
									"key": "name",
									"value": "Иван"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "13. Get Single Employee",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => {\r",
									"    pm.response.to.have.status(200);\r",
									"});\r",
									"\r",
									"pm.test(\"Valid employee structure\", () => {\r",
									"    const employee = pm.response.json();\r",
									"    pm.expect(employee).to.include.keys([\r",
									"        \"id\", \"name\", \"surname\", \"emailAddress\", \"position\"\r",
									"    ]);\r",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_employees_get}}/{{employeeId}}",
							"host": [
								"{{URL_employees_get}}"
							],
							"path": [
								"{{employeeId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "14. Get All Employees - Size 5",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Pagination works\", () => {\r",
									"    const res = pm.response.json();\r",
									"    pm.expect(res.content.length).to.eql(5);\r",
									"    pm.expect(res.totalElements).to.be.at.least(10);\r",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_employees_get}}?size=5",
							"host": [
								"{{URL_employees_get}}"
							],
							"query": [
								{
									"key": "size",
									"value": "5"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "15. Delete Employee - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 204 No Content\", () => pm.response.to.have.status(204));"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "{{URL_employees_delete}}{{employeeId}}",
							"host": [
								"{{URL_employees_delete}}{{employeeId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "16. Delete Employee - Not Found",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 404 Not Found\", () => pm.response.to.have.status(404));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Нет сотрудника с запрошенным id');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "{{URL_employees_delete}}999999",
							"host": [
								"{{URL_employees_delete}}999999"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Clients",
			"item": [
				{
					"name": "1. Create Client - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 201 Created\", () => pm.response.to.have.status(201));",
									"pm.test(\"Response has ID\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json).to.have.property('id');",
									"    pm.environment.set('clientId', json.id);",
									"});",
									"pm.test(\"Response contains all fields\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json.name).to.eql(\"Анна\");",
									"    pm.expect(json.surname).to.eql(\"Смирнова\");",
									"    pm.expect(json.emailAddress).to.eql(\"anna@example.com\");",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Анна\",\n  \"surname\": \"Смирнова\",\n  \"emailAddress\": \"anna@example.com\",\n  \"phoneNumber\": \"1234567890\"\n}"
						},
						"url": {
							"raw": "{{URL_clients_create}}",
							"host": [
								"{{URL_clients_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "2. Create Client - Missing Required Fields",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Contains all required field errors\", () => {",
									"    const responseText = pm.response.text();",
									"    pm.expect(responseText).to.include('Имя обязательно');",
									"    pm.expect(responseText).to.include('Фамилия обязательна');",
									"    pm.expect(responseText).to.include('Email обязателен');",
									"    pm.expect(responseText).to.include('Телефон обязателен');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"\",\n  \"surname\": \"\",\n  \"emailAddress\": \"\",\n  \"phoneNumber\": \"\"\n}"
						},
						"url": {
							"raw": "{{URL_clients_create}}",
							"host": [
								"{{URL_clients_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "3. Create Client - Invalid Email Format",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Некорректный формат email');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Мария\",\n  \"surname\": \"Иванова\",\n  \"emailAddress\": \"invalid-email\",\n  \"phoneNumber\": \"9876543210\"\n}"
						},
						"url": {
							"raw": "{{URL_clients_create}}",
							"host": [
								"{{URL_clients_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "4. Create Client - Invalid Phone Format",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Некорректный формат телефонного номера');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Елена\",\n  \"surname\": \"Петрова\",\n  \"emailAddress\": \"elena@example.com\",\n  \"phoneNumber\": \"123\"\n}"
						},
						"url": {
							"raw": "{{URL_clients_create}}",
							"host": [
								"{{URL_clients_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "5. Create Client - Duplicate Email",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 409 Conflict\", () => pm.response.to.have.status(409));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Email уже используется');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Ольга\",\n  \"surname\": \"Петрова\",\n  \"emailAddress\": \"anna@example.com\",\n  \"phoneNumber\": \"9876543210\"\n}"
						},
						"url": {
							"raw": "{{URL_clients_create}}",
							"host": [
								"{{URL_clients_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "6. Create Client - Duplicate Phone",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 409 Conflict\", () => pm.response.to.have.status(409));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Телефон уже используется');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Ольга\",\n  \"surname\": \"Петрова\",\n  \"emailAddress\": \"olga@example.com\",\n  \"phoneNumber\": \"1234567890\"\n}"
						},
						"url": {
							"raw": "{{URL_clients_create}}",
							"host": [
								"{{URL_clients_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "7. Get Client Default - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Должен вернуть первую страницу с 10 элементами\", () => {",
									"  pm.response.to.have.status(200);",
									"  const res = pm.response.json();",
									"  pm.expect(res.size).to.eql(10);",
									"  pm.expect(res.number).to.eql(0);",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						},
						{
							"listen": "prerequest",
							"script": {
								"packages": {},
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_clients_get}}",
							"host": [
								"{{URL_clients_get}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "8. Get Client - Not Found",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 404 Not Found\", () => pm.response.to.have.status(404));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Клиент не найден');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_clients_get}}/999999",
							"host": [
								"{{URL_clients_get}}"
							],
							"path": [
								"999999"
							]
						}
					},
					"response": []
				},
				{
					"name": "9. Get Client Custom - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Должен вернуть вторую страницу с 5 элементами\", () => {\r",
									"  const res = pm.response.json();\r",
									"  pm.expect(res.size).to.eql(5);\r",
									"  pm.expect(res.number).to.eql(1);\r",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_clients_get}}?page=1&size=5",
							"host": [
								"{{URL_clients_get}}"
							],
							"query": [
								{
									"key": "page",
									"value": "1"
								},
								{
									"key": "size",
									"value": "5"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "10. Get Client Desc - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Должен вернуть клиентов, отсортированных по имени Z→A\", () => {\r",
									"  const res = pm.response.json();\r",
									"  for (let i = 0; i < res.content.length-1; i++) {\r",
									"    pm.expect(\r",
									"      res.content[i].name.localeCompare(res.content[i+1].name)\r",
									"    ).to.be.at.least(0);\r",
									"  }\r",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_clients_get}}?sortBy=name&direction=desc",
							"host": [
								"{{URL_clients_get}}"
							],
							"query": [
								{
									"key": "sortBy",
									"value": "name"
								},
								{
									"key": "direction",
									"value": "desc"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "11. Get Client Email - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Должен вернуть только клиентов с email, содержащим 'example.com'\", () => {\r",
									"  const res = pm.response.json();\r",
									"  res.content.forEach(client => {\r",
									"    pm.expect(client.emailAddress).to.include(\"example.com\");\r",
									"  });\r",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_clients_get}}?email=example.com",
							"host": [
								"{{URL_clients_get}}"
							],
							"query": [
								{
									"key": "email",
									"value": "example.com"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "12. Get Client HasOrders - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Должен вернуть только клиентов с заказами\", () => {\r",
									"  const res = pm.response.json();\r",
									"  res.content.forEach(client => {\r",
									"    pm.expect(client.orders?.length).to.be.greaterThan(0);\r",
									"  });\r",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_clients_get}}?hasOrders=true",
							"host": [
								"{{URL_clients_get}}"
							],
							"query": [
								{
									"key": "hasOrders",
									"value": "true"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "14. Update Client - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Phone updated\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json.phoneNumber).to.eql(\"9876543210\");",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "PATCH",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"phoneNumber\": \"9876543210\"\n}"
						},
						"url": {
							"raw": "{{URL_clients_update}}{{clientId}}",
							"host": [
								"{{URL_clients_update}}{{clientId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "15. Update Client - Invalid Email",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Некорректный формат email');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "PATCH",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"emailAddress\": \"invalid-email\"\n}"
						},
						"url": {
							"raw": "{{URL_clients_update}}{{clientId}}",
							"host": [
								"{{URL_clients_update}}{{clientId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "16. Update Client - Duplicate Phone",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 409 Conflict\", () => pm.response.to.have.status(409));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Номер телефона уже существует');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "PATCH",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"phoneNumber\": \"1234567890\"\n}"
						},
						"url": {
							"raw": "{{URL_clients_update}}{{clientId}}",
							"host": [
								"{{URL_clients_update}}{{clientId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "17. Get All Clients - No Filter",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Returns paginated response\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json).to.have.property('content');",
									"    pm.expect(json).to.have.property('totalElements');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_clients_get}}",
							"host": [
								"{{URL_clients_get}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "18. Get All Clients - Filter by Email",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Email filter works\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json.content[0].emailAddress).to.eql(\"anna@example.com\");",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_clients_get}}?email=anna@example.com",
							"host": [
								"{{URL_clients_get}}"
							],
							"query": [
								{
									"key": "email",
									"value": "anna@example.com"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "19. Get All Clients - Filter by HasOrders",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"HasOrders filter works\", () => {",
									"    const json = pm.response.json();",
									"    json.content.forEach(client => {",
									"        pm.expect(client.orders).to.not.be.empty;",
									"    });",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_clients_get}}?hasOrders=true",
							"host": [
								"{{URL_clients_get}}"
							],
							"query": [
								{
									"key": "hasOrders",
									"value": "true"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "21. Delete Client - Not Found",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 404 Not Found\", () => pm.response.to.have.status(404));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Нет клиента с запрошенным id');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "{{URL_clients_delete}}999999",
							"host": [
								"{{URL_clients_delete}}999999"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Products",
			"item": [
				{
					"name": "1. Create Product - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 201 Created\", () => pm.response.to.have.status(201));",
									"pm.test(\"Response has ID\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json).to.have.property('id');",
									"    pm.environment.set('productId', json.id);",
									"});",
									"pm.test(\"Response contains all fields\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json.name).to.eql(\"Ноутбук\");",
									"    pm.expect(json.description).to.eql(\"Игровой\");",
									"    pm.expect(json.price).to.eql(1500);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Ноутбук\",\n  \"description\": \"Игровой\",\n  \"price\": 1500\n}"
						},
						"url": {
							"raw": "{{URL_products_create}}",
							"host": [
								"{{URL_products_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "2. Create Product - Missing Required Fields",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Contains all required field errors\", () => {",
									"    const responseText = pm.response.text();",
									"    pm.expect(responseText).to.include('Название обязательно');",
									"    pm.expect(responseText).to.include('Описание обязательно');",
									"    pm.expect(responseText).to.include('Цена обязательна');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"\",\n  \"description\": \"\",\n  \"price\": null\n}"
						},
						"url": {
							"raw": "{{URL_products_create}}",
							"host": [
								"{{URL_products_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "3. Create Product - Negative Price",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Цена обязательна');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Телефон\",\n  \"description\": \"Смартфон\",\n  \"price\": -100\n}"
						},
						"url": {
							"raw": "{{URL_products_create}}",
							"host": [
								"{{URL_products_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "4. Create Product - Duplicate Name",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 409 Conflict\", () => pm.response.to.have.status(409));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Название должно быть уникальным');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"name\": \"Ноутбук\",\n  \"description\": \"Другой ноутбук\",\n  \"price\": 2000\n}"
						},
						"url": {
							"raw": "{{URL_products_create}}",
							"host": [
								"{{URL_products_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "5. Get Product - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"",
									"pm.test(\"Response contains valid product data\", () => {",
									"    const json = pm.response.json();",
									"    ",
									"    // Проверяем что есть хотя бы один продукт",
									"    pm.expect(json.content.length).to.be.greaterThan(0);",
									"    ",
									"    // Проверяем структуру каждого продукта",
									"    json.content.forEach(product => {",
									"        pm.expect(product).to.have.property('id').that.is.a('number');",
									"        pm.expect(product).to.have.property('name').that.is.a('string');",
									"        pm.expect(product).to.have.property('description').that.is.a('string');",
									"        pm.expect(product).to.have.property('price').that.is.a('number');",
									"    });",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_products_get}}",
							"host": [
								"{{URL_products_get}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "6. Get Product - Not Found",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 404 Not Found\", () => pm.response.to.have.status(404));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Товар не найден');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_products_get}}/999999",
							"host": [
								"{{URL_products_get}}"
							],
							"path": [
								"999999"
							]
						}
					},
					"response": []
				},
				{
					"name": "7. Update Product - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Description updated\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json.description).to.eql(\"Новое описание ноутбука\");",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "PATCH",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "Новое описание ноутбука"
						},
						"url": {
							"raw": "{{URL_products_update}}{{productId}}",
							"host": [
								"{{URL_products_update}}{{productId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "8. Update Product - Empty Description",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Описание обязательно');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "PATCH",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "\"\""
						},
						"url": {
							"raw": "{{URL_products_update}}{{productId}}",
							"host": [
								"{{URL_products_update}}{{productId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "9. Get All Products - No Filter",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Returns paginated response\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json).to.have.property('content');",
									"    pm.expect(json).to.have.property('totalElements');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_products_get}}",
							"host": [
								"{{URL_products_get}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "10. Get All Products - Filter by Name",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Name filter works\", () => {",
									"    const json = pm.response.json();",
									"    json.content.forEach(product => {",
									"        pm.expect(product.name).to.include(\"Ноутбук\");",
									"    });",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_products_get}}?name=Ноутбук",
							"host": [
								"{{URL_products_get}}"
							],
							"query": [
								{
									"key": "name",
									"value": "Ноутбук"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "11. Get All Products - Filter by Price",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Price filter works\", () => {",
									"    const json = pm.response.json();",
									"    json.content.forEach(product => {",
									"        pm.expect(product.price).to.eql(1500);",
									"    });",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_products_get}}?price=1500",
							"host": [
								"{{URL_products_get}}"
							],
							"query": [
								{
									"key": "price",
									"value": "1500"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "13. Delete Product - Not Found",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 404 Not Found\", () => pm.response.to.have.status(404));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Нет продукта с запрошенным id');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "{{URL_products_delete}}999999",
							"host": [
								"{{URL_products_delete}}999999"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Orders",
			"item": [
				{
					"name": "1. Create Order - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 201 Created\", () => pm.response.to.have.status(201));",
									"pm.test(\"Response has ID\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json).to.have.property('id');",
									"    pm.environment.set('orderId', json.id);",
									"});",
									"pm.test(\"Response contains all fields\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json.status).to.eql(\"NEW\");",
									"    pm.expect(json.clientId).to.eql(parseInt(pm.environment.get('clientId')));",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"status\": \"NEW\",\n  \"clientId\": 1,\n  \"productsId\": [1]\n}"
						},
						"url": {
							"raw": "{{URL_orders_create}}",
							"host": [
								"{{URL_orders_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "2. Create Order - Invalid Status",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Статус должен быть');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"clientId\": {{clientId}},\n  \"productIds\": [{{productId}}],\n  \"status\": \"INVALID\"\n}"
						},
						"url": {
							"raw": "{{URL_orders_create}}",
							"host": [
								"{{URL_orders_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "3. Create Order - Missing Status",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Статус обязателен');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"clientId\": {{clientId}},\n  \"productIds\": [{{productId}}]\n}"
						},
						"url": {
							"raw": "{{URL_orders_create}}",
							"host": [
								"{{URL_orders_create}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "6. Update Order - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Status updated\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json.status).to.eql(\"PROCESSING\");",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "PATCH",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "\"PROCESSING\""
						},
						"url": {
							"raw": "{{URL_orders_update}}{{orderId}}",
							"host": [
								"{{URL_orders_update}}{{orderId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "7. Update Order - Invalid Status",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 400 Bad Request\", () => pm.response.to.have.status(400));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Статус должен быть');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "PATCH",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "\"INVALID\""
						},
						"url": {
							"raw": "{{URL_orders_update}}{{orderId}}",
							"host": [
								"{{URL_orders_update}}{{orderId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "8. Get All Orders - No Filter",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Returns paginated response\", () => {",
									"    const json = pm.response.json();",
									"    pm.expect(json).to.have.property('content');",
									"    pm.expect(json).to.have.property('totalElements');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_orders_get}}",
							"host": [
								"{{URL_orders_get}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "9. Get All Orders - Filter by Status",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));",
									"pm.test(\"Status filter works\", () => {",
									"    const json = pm.response.json();",
									"    json.content.forEach(order => {",
									"        pm.expect(order.status).to.eql(\"NEW\");",
									"    });",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_orders_get}}?status=NEW",
							"host": [
								"{{URL_orders_get}}"
							],
							"query": [
								{
									"key": "status",
									"value": "NEW"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "10. Get All Orders - Filter by CreatedAt",
					"event": [
						{
							"listen": "prerequest",
							"script": {
								"exec": [
									"const now = new Date().toISOString();",
									"pm.environment.set('currentDate', now.split('T')[0]);"
								],
								"type": "text/javascript"
							}
						},
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 200 OK\", () => pm.response.to.have.status(200));"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{URL_orders_get}}?createdAt={{currentDate}}",
							"host": [
								"{{URL_orders_get}}"
							],
							"query": [
								{
									"key": "createdAt",
									"value": "{{currentDate}}"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "11. Delete Order - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 204 No Content\", () => pm.response.to.have.status(204));"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "{{URL_orders_delete}}{{orderId}}",
							"host": [
								"{{URL_orders_delete}}{{orderId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "12. Delete Order - Not Found",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 404 Not Found\", () => pm.response.to.have.status(404));",
									"pm.test(\"Correct error message\", () => {",
									"    pm.expect(pm.response.text()).to.include('Нет заказа с запрошенным id');",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "{{URL_orders_delete}}999999",
							"host": [
								"{{URL_orders_delete}}999999"
							]
						}
					},
					"response": []
				},
				{
					"name": "20. Delete Client - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 204 No Content\", () => pm.response.to.have.status(204));"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "{{URL_clients_delete}}{{clientId}}",
							"host": [
								"{{URL_clients_delete}}{{clientId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "12. Delete Product - Valid",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status 204 No Content\", () => pm.response.to.have.status(204));"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "{{URL_products_delete}}{{productId}}",
							"host": [
								"{{URL_products_delete}}{{productId}}"
							]
						}
					},
					"response": []
				}
			]
		}
	],
	"event": [
		{
			"listen": "prerequest",
			"script": {
				"type": "text/javascript",
				"packages": {},
				"exec": [
					""
				]
			}
		},
		{
			"listen": "test",
			"script": {
				"type": "text/javascript",
				"packages": {},
				"exec": [
					""
				]
			}
		}
	],
	"variable": [
		{
			"key": "baseUrl",
			"value": ""
		},
		{
			"key": "URL_clients_create",
			"value": ""
		},
		{
			"key": "URL_clients_delete",
			"value": ""
		},
		{
			"key": "URL_clients_get",
			"value": ""
		},
		{
			"key": "URL_clients_update",
			"value": ""
		},
		{
			"key": "URL_employees_create",
			"value": ""
		},
		{
			"key": "URL_employees_delete",
			"value": ""
		},
		{
			"key": "URL_employees_get",
			"value": ""
		},
		{
			"key": "URL_employees_update",
			"value": ""
		},
		{
			"key": "URL_orders_create",
			"value": ""
		},
		{
			"key": "URL_orders_delete",
			"value": ""
		},
		{
			"key": "URL_orders_get",
			"value": ""
		},
		{
			"key": "URL_orders_update",
			"value": ""
		},
		{
			"key": "URL_products_create",
			"value": ""
		},
		{
			"key": "URL_products_delete",
			"value": ""
		},
		{
			"key": "URL_products_get",
			"value": ""
		},
		{
			"key": "URL_products_update",
			"value": ""
		}
	]
}
