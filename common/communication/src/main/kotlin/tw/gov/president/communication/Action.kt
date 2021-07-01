package tw.gov.president.communication

enum class Action(var action_code:Int,var action_name:String) {
    SEND_MESSAGE(1, "sendmessage"),
    PLAY_GAME(2, "play_game"),
}