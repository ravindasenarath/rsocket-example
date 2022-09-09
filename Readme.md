Commands

 - rsc --debug --request --data "{\"message\":\"Hello\"}" --route request-response --stacktrace tcp://localhost:7000
 - rsc --debug --fnf --data "{\"message\":\"Hello\"}" --route fire-and-forget --stacktrace tcp://localhost:7000
 - rsc --debug --stream --data "{\"message\":\"Hello\"}" --route request-stream --stacktrace tcp://localhost:7000
 - rsc --debug --channel --data - --route channel --stacktrace tcp://localhost:7000