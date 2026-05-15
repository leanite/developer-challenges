package com.leanite.dynaquiz.feature.quiz.res

import androidx.compose.ui.unit.dp
import dynaquiz.composeapp.generated.resources.Res
import dynaquiz.composeapp.generated.resources.quiz_button_continue
import dynaquiz.composeapp.generated.resources.quiz_button_exit
import dynaquiz.composeapp.generated.resources.quiz_dialog_exit_body
import dynaquiz.composeapp.generated.resources.quiz_dialog_exit_title
import dynaquiz.composeapp.generated.resources.quiz_loading_question
import dynaquiz.composeapp.generated.resources.quiz_msg_question_load_failed
import dynaquiz.composeapp.generated.resources.quiz_msg_answer_submit_failed
import dynaquiz.composeapp.generated.resources.quiz_topbar_progress

internal object QuizRes {
    val TopBarProgress = Res.string.quiz_topbar_progress           
    val LoadingQuestion = Res.string.quiz_loading_question
    val DialogExitTitle = Res.string.quiz_dialog_exit_title
    val DialogExitBody = Res.string.quiz_dialog_exit_body
    val ButtonExit = Res.string.quiz_button_exit
    val ButtonContinue = Res.string.quiz_button_continue
    val MsgQuestionLoadFailed = Res.string.quiz_msg_question_load_failed
    val MsgAnswerSubmitFailed = Res.string.quiz_msg_answer_submit_failed

    object Dimensions {
        val TimerHeight = 88.dp
    }
}