# Cenários: Quiz

## Início e countdown

### Cenário: Started should start countdown at 3 and decrement each second to zero
**Dado** o ViewModel foi instanciado em modo Timed Easy
**Quando** `Intent.Started` é disparado
**Então** `phase` é `Countdown(3)` no primeiro instante
**E** após 1s `phase` é `Countdown(2)`
**E** após 2s `phase` é `Countdown(1)`

### Cenário: Started should prefetch the first question in parallel with the countdown
**Dado** o `GetRandomQuestionUseCase` retornará `Success(questionA)`
**Quando** `Intent.Started` é disparado
**Então** o fetch da primeira pergunta acontece durante o countdown
**E** ao fim do countdown a phase passa para `Playing(questionA)` imediatamente

### Cenário: Started with failure on first question fetch should emit QuestionLoadFailed and NavigateBack
**Dado** o `GetRandomQuestionUseCase` retornará `Error`
**Quando** `Intent.Started` é disparado e o countdown termina
**Então** é emitido `Event.ShowMessage(QuestionLoadFailed)`
**E** é emitido `Event.NavigateBack`

## Timer (modo Timed)

### Cenário: Playing in Timed mode should initialize timeRemainingSec with perQuestionSeconds
**Dado** o ViewModel está em modo `Timed.Hard` (10s)
**Quando** a phase entra em `Playing`
**Então** `timeRemainingSec` é `10`

### Cenário: timer should decrement each second during Playing
**Dado** a phase está em `Playing` com `timeRemainingSec` igual a `perQuestionSeconds`
**Quando** 1 segundo se passa
**Então** `timeRemainingSec` é decrementado em 1

### Cenário: timer reaching zero should register TimedOut log and advance to the next question
**Dado** a phase está em `Playing(questionA)` no modo Timed
**E** o `GetRandomQuestionUseCase` retornará `Success(questionB)` para a próxima
**Quando** o timer chega a zero
**Então** o `answerLog` ganha uma entrada `outcome = TimedOut` para `questionA`
**E** `currentQuestionIndex` é incrementado
**E** a phase passa para `Playing(questionB)`

### Cenário: Playing in Relaxed mode should keep timeRemainingSec null
**Dado** o ViewModel está em modo `Relaxed`
**Quando** a phase entra em `Playing`
**Então** `timeRemainingSec` é `null`

## Seleção de resposta

### Cenário: AnswerSelected should mark selectedAnswer and isSubmitting immediately
**Dado** a phase está em `Playing(questionA)`
**Quando** `Intent.AnswerSelected("B")` é disparado
**Então** `phase.selectedAnswer` é `"B"`
**E** `phase.isSubmitting` é `true`

### Cenário: AnswerSelected should cancel the running timer
**Dado** a phase está em `Playing` no modo Timed com 9s restantes
**Quando** `Intent.AnswerSelected("A")` é disparado e o submit confirma
**E** o tempo avança 2 segundos
**Então** `timeRemainingSec` não decrementou (ou já estava em fluxo de próxima pergunta)

### Cenário: AnswerSelected while isSubmitting should be ignored
**Dado** a phase está em `Playing` com `isSubmitting = true`
**Quando** outro `Intent.AnswerSelected("C")` é disparado
**Então** o `SubmitAnswerUseCase` não é chamado uma segunda vez

### Cenário: AnswerSelected with Success Confirmed correct should register log Confirmed correct true
**Dado** `SubmitAnswerUseCase` retornará `Success(Answer(correct = true))`
**Quando** `Intent.AnswerSelected("B")` é disparado e o quiz avança
**Então** o último log no `answerLog` tem `outcome = Confirmed(correct = true)`

### Cenário: AnswerSelected with Success Confirmed incorrect should register log Confirmed correct false
**Dado** `SubmitAnswerUseCase` retornará `Success(Answer(correct = false))`
**Quando** `Intent.AnswerSelected("B")` é disparado e o quiz avança
**Então** o último log no `answerLog` tem `outcome = Confirmed(correct = false)`

### Cenário: AnswerSelected with Error should emit AnswerSubmitFailed and register outcome SubmitFailed
**Dado** `SubmitAnswerUseCase` retornará `Error`
**Quando** `Intent.AnswerSelected("B")` é disparado
**Então** é emitido `Event.ShowMessage(AnswerSubmitFailed)`
**E** o último log no `answerLog` tem `outcome = SubmitFailed`

## Prefetch da próxima pergunta

### Cenário: after entering Playing the next question should be prefetched
**Dado** a phase entrou em `Playing(questionA)` e há mais perguntas a vir
**Quando** o ViewModel finaliza a transição para Playing
**Então** o `GetRandomQuestionUseCase` é chamado uma segunda vez (prefetch da próxima)

### Cenário: prefetch should not be scheduled on the last question
**Dado** a phase entrou em `Playing` para a 10ª pergunta
**Quando** o ViewModel finaliza a transição
**Então** o `GetRandomQuestionUseCase` é chamado uma única vez (sem prefetch)

## Conclusão

### Cenário: answering the tenth question should mark phase Completed
**Dado** o quiz está na 10ª pergunta
**Quando** `Intent.AnswerSelected("X")` é disparado e o submit retorna
**Então** `phase` é `Completed`
**E** `currentQuestionIndex` é `10`

### Cenário: completion should call SaveQuizSessionUseCase with the score computed from the answerLog
**Dado** o jogador acertou todas as 10 no modo Relaxed
**Quando** o quiz é concluído
**Então** o `SaveQuizSessionUseCase` é chamado com `score = Score(10)` e `correctAnswers = 10`

### Cenário: completion should emit NavigateToResult with the QuizSessionResult
**Dado** o quiz foi concluído
**Quando** o ViewModel sinaliza a conclusão
**Então** é emitido `Event.NavigateToResult` com o `QuizSessionResult` correspondente

## Exit dialog

### Cenário: BackPressed should open the exit dialog
**Dado** a phase está em `Playing`
**Quando** `Intent.BackPressed` é disparado
**Então** `showExitDialog` é `true`

### Cenário: ExitCancelled should close the dialog keeping the running quiz
**Dado** `showExitDialog` é `true` e a phase está em `Playing`
**Quando** `Intent.ExitCancelled` é disparado
**Então** `showExitDialog` é `false`
**E** a phase continua sendo `Playing`

### Cenário: ExitConfirmed should close the dialog and emit NavigateBack
**Dado** `showExitDialog` é `true`
**Quando** `Intent.ExitConfirmed` é disparado
**Então** `showExitDialog` é `false`
**E** é emitido `Event.NavigateBack`
