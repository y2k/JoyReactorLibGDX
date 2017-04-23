package y2k.joyreactor.views

import im.y2k.libgdxdsl.*
import y2k.joyreactor.domain.Iteractor
import y2k.joyreactor.domain.Message
import java.text.SimpleDateFormat

/**
* Created by y2k on 02/12/2016.
**/
class MessagesPage(parent: GroupEx) {

    init {
        parent.setView {
            stackV {
                toolbar("Messages")

                scrollV {
                    Iteractor.getMessages().thenAccept {
                        for (m in it) item(m)
                    }
                }
                layout(height = LayoutParam.fill)

                enterUserMessage()
                layout(width = LayoutParam.fill)
            }
        }
    }

    private fun GroupEx.item(m: Message) {
        stackH {
            space {
                width = if (m.isMine) 50 else 0
            }
            stackV {
                label {
                    padding = 5
                    text = m.message
                }
                label {
                    padding = 5
                    text = SimpleDateFormat("HH:mm dd MMM").format(m.date)
                }
            }
            layout(width = LayoutParam.fill)
            space {
                width = if (!m.isMine) 50 else 0
            }
        }
    }

    private fun GroupEx.enterUserMessage() {
        stackH {
            edit {
                hint = "Enter message"
            }
            layout(width = LayoutParam.fill)

            button {
                text = "Send"
                click {
                    Iteractor.sendUserMessage("")
                }
            }
        }
    }
}