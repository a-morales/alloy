$version: "2"

namespace alloy.test

use alloy.test#CustomCode
use alloy#simpleRestJson
use smithy.test#httpRequestTests
use smithy.test#httpResponseTests

apply CustomCode @httpRequestTests([
    {
        id: "CustomCodeInput"
        documentation: "tests custom code as a label",
        protocol: simpleRestJson
        uri: "/custom-code/399"
        method: "GET"
        body: ""
        params: {
            "code": 399
        }
    }
])
apply CustomCode @httpResponseTests([
    {
        id: "CustomCodeOutput"
        documentation: "respect the httpResponseCode trait",
        protocol: simpleRestJson
        code: 399
        body: ""
        params: {
            "code": 399
        }
    }
])
