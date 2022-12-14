package com.remember.unit_test.user.request

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

class SampleControllerSpec : RequestSpecHelper() {

    init {

        lateinit var api: SampleController
        lateinit var mockMvc: MockMvc

        beforeTest {
            api = SampleController()
            mockMvc = mockMvcSetup(api)
        }

        describe("PathVariable 매핑") {
            it("URL 매핑 확인") {

                mockMvc.get("/groups/2/targets/1/samples/3") {

                }.andExpect { status { isOk() } }
            }
        }
    }
}

@RestController
class SampleController {

    @GetMapping("/groups/{groupId}/targets/{targetId}/samples/{sampleId}")
    fun sample(
        @PathVariable groupId: Long,
        @PathVariable targetId: Long,
        @PathVariable sampleId: Long
    ) {
        println(groupId)
        println(targetId)
        println(sampleId)
    }
}
