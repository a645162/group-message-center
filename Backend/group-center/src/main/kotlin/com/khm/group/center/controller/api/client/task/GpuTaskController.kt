package com.khm.group.center.controller.api.client.task

import com.khm.group.center.datatype.config.MachineConfig
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

import com.khm.group.center.datatype.receive.GpuTaskInfo
import com.khm.group.center.datatype.response.ClientResponse


@RestController
class GpuTaskController {

    @Operation(summary = "Test")
    @RequestMapping("/api/client/gpu_task/test", method = [RequestMethod.GET])
    fun test(): ClientResponse {
        val responseObj = ClientResponse()
        responseObj.result = "success"
        responseObj.isAuthenticated = true
        return responseObj
    }

    @Operation(summary = "GPU任务变动")
    @RequestMapping("/api/client/gpu_task/info", method = [RequestMethod.POST])
    fun gpuTaskInfo(@RequestBody gpuTaskInfo: GpuTaskInfo): ClientResponse {
        // Notify
        val machineConfig = MachineConfig.getMachineByNameEng(gpuTaskInfo.serverNameEng)

        val gpuTaskNotify = GpuTaskNotify(
            gpuTaskInfo = gpuTaskInfo,
            machineConfig = machineConfig
        )

        if (gpuTaskInfo.multiDeviceLocalRank < 1) {
            gpuTaskNotify.sendTaskMessage()
        }

        val responseObj = ClientResponse()
        responseObj.result = "success"
        responseObj.isSucceed = true
        responseObj.isAuthenticated = true
        return responseObj
    }

}
