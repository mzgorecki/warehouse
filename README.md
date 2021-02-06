# data warehouse demo

## Description
A simple data warehouse querying interface.
Built using Java, Spring Boot and H2 as database.

## Example requests
Total clicks for "Google Ads" datasource for "11/12/19"
```
curl -X GET \
  http://46.41.138.71:8080/api/query \
  -H 'Content-Type: application/json' \
  -d '{
	"metricOperations": [ {
		"operation": "NONE",
		"metric": "CLICKS"
	},
	{
		"operation": "NONE",
		"metric": "IMPRESSIONS"
	},
	{
		"operation": "NONE",
		"metric": "CTR"
	}],
	"dimensionFilters": [{
		"dimension":"DATASOURCE",
		"value": "Google Ads"
		
	},{
		"dimension":"DATE",
		"value": "11/12/19"
	}]
}'
```

CTR per datasource and campaign:
```
curl -X GET \
  http://46.41.138.71:8080/api/query \
  -H 'Content-Type: application/json' \
  -d '{
	"metricOperations": [ {
		"operation": "AVG",
		"metric": "CTR"
	}],
	"dimensionGroupings": [ "DATE", "CAMPAIGN" ]
}'
```

Impressions over time:
```
curl -X GET \
  http://46.41.138.71:8080/api/query \
  -H 'Content-Type: application/json' \
  -d '{
	"metricOperations": [ {
		"operation": "AVG",
		"metric": "IMPRESSIONS"
	}],
	"dimensionGroupings": [ "DATE"]
}'
```
