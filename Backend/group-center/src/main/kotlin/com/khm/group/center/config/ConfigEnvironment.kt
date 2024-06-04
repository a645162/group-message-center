package com.khm.group.center.config

import com.khm.group.center.utils.datetime.DateTime

class ConfigEnvironment {

    companion object {
        var FILE_ENV_LIST: HashMap<String, String> = HashMap()

        var PASSWORD_JWT: String = ""

        fun initializeConfigEnvironment() {
            initializeFileEnvList()
            initializePasswordJwt()
        }

        private fun initializeFileEnvList() {
            // Read File
            val fileEnvList = System.getenv("FILE_ENV_PATH") ?: ""

        }

        private fun initializePasswordJwt() {
            PASSWORD_JWT = System.getenv("PASSWORD_JWT") ?: ""
            if (PASSWORD_JWT.trim { it <= ' ' }.isEmpty()) {
                PASSWORD_JWT = DateTime.getCurrentDateTimeStr()
            }
        }
    }

}