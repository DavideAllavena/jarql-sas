# jarql-server

A version of Jarql that exposes a webserver and convert your triples. EXPERIMENTAL 

# HowTo

This tool exposes the services on port 8080. Make a post request sending data as a Json made like this:
```
{	"data": {
		"your": "json"
	},
	"query": "construct {?s ?p ?o} where {?s ?p ?o }"
}
```

The `data` contains your JSON that should be converted

The `query` key contains the SPARQL construct as a string.

# TODO

* Use Jarql as dependency solving this issue (maybe updating jarql dependency)
  ```
  SLF4J: The requested version 1.5.8 by your slf4j binding is not compatible with [1.6, 1.7]
  ```
* make checks input
* better readme
* export parameters
* make it multithread
* increase preformances
