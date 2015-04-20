# A basic set of requests to test things out locally

#Get Stats
curl -v 'localhost:8900/champions/10/stats?start_date=2015-04-05T00:00:00.000Z&end_date=2015-04-06T00:00.000Z' | jq .

#Get All Champions and Image URLS
curl -v 'localhost:8900/champions' | jq .