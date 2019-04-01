#Auto Test

Test cases written against virtualized testbox. Pact tests run to make sure the
mocks are up to date with what the consumer expects


##Pact Commands

###Pact Publish contracts from consumer side
mvn pact:publish

### Pact Verify contracts from provider side
mvn pact:verify

