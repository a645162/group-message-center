package com.khm.group.center.message

import com.khm.group.center.datatype.config.GroupUserConfig
import com.khm.group.center.message.webhook.lark.LarkBot
import com.khm.group.center.message.webhook.lark.LarkGroupBot
import com.khm.group.center.message.webhook.wecom.WeComGroupBot


class MessageSender(private val messageItem: MessageItem) {

    private val userConfig: GroupUserConfig? =
        GroupUserConfig.getUserByName(messageItem.targetUser)

    fun sendMessage() {
        if (!messageItem.machineConfig.haveValidWebHookService()) {
            println("No any valid webhook server.")
            return
        }

        if (messageItem.machineConfig.weComServer.enable) {
            sendByWeWork()
        }
        if (messageItem.machineConfig.larkServer.enable) {
            sendByLark()
        }
    }

    private fun sendByWeWork() {
        val groupKey = messageItem.machineConfig.weComServer.groupBotKey
        val url = WeComGroupBot.getWebhookUrl(groupKey)

        val mentionedIdList = ArrayList<String>()
        val mentionedMobileList = ArrayList<String>()

        if (userConfig != null) {
            val userId = userConfig.weCom.userId
            if (userId.isNotEmpty())
                mentionedIdList.add(userId)

            val userMobilePhone = userConfig.weCom.userMobilePhone
            if (userMobilePhone.isNotEmpty())
                mentionedMobileList.add(userMobilePhone)
        }

        WeComGroupBot.directSendTextWithUrl(
            url, messageItem.content,
            mentionedIdList,
            mentionedMobileList
        )
    }

    private fun sendByLark() {
        val machineName = messageItem.machineConfig.name
        val machineUrl = "http://" + messageItem.machineConfig.host

        val groupBotId = messageItem.machineConfig.larkServer.groupBotId
        val groupBotKey = messageItem.machineConfig.larkServer.groupBotKey

        val larkGroupBotObj = LarkGroupBot(groupBotId, groupBotKey)

        var atText = ""
        if (userConfig != null) {
            val userId = userConfig.lark.userId
            if (userId.isEmpty()) {
                atText = userConfig.name
            } else {
                atText = LarkGroupBot.getAtUserHtml(userId)

                if (LarkBot.isAppIdSecretValid()) {
                    val larkBotObj = LarkBot(userConfig.lark.userId)

                    larkBotObj.sendText(
                        messageItem.content.trim()
                                + "\n\n"
                                + "${machineName}:\n"
                                + machineUrl
                    )
                }
            }
        }
        val finalText = atText + messageItem.content

        larkGroupBotObj.sendText(
            finalText
                    + "\n\n"
                    + machineUrl
        )
    }
}
