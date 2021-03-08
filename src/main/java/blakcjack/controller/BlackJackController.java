package blakcjack.controller;

import blakcjack.domain.blackjackgame.BlackjackGame;
import blakcjack.domain.blackjackgame.GameInitializationFailureException;
import blakcjack.domain.card.Deck;
import blakcjack.domain.card.EmptyDeckException;
import blakcjack.domain.participant.Dealer;
import blakcjack.domain.participant.Participant;
import blakcjack.exception.GameTerminationException;
import blakcjack.view.InputView;

import blakcjack.view.OutputView;

import java.util.List;

import static blakcjack.view.InputView.takePlayerNamesInput;
import static blakcjack.view.OutputView.printGameClosing;
import static blakcjack.view.OutputView.printInitialHands;

public class BlackJackController {
    public void run() {
        final BlackjackGame blackjackGame = initializeGame();
        final List<Participant> players = blackjackGame.getPlayers();
        final Dealer dealer = (Dealer) blackjackGame.getDealer();

        drawInitialCards(blackjackGame);
        printInitialHands(dealer, players);

        drawCardsInTurn(blackjackGame, players, dealer);

        notifyFinalSummary(blackjackGame, players, dealer);
    }

    private BlackjackGame initializeGame() {
        try {
            return new BlackjackGame(new Deck(), takePlayerNamesInput());
        } catch (GameInitializationFailureException e) {
            printGameClosing(e.getMessage());
            throw new GameTerminationException();
        }
    }

    private void drawInitialCards(final BlackjackGame blackjackGame) {
        try {
            blackjackGame.initializeHands();
        } catch (final EmptyDeckException e) {
            printGameClosing(e.getMessage());
            throw new GameTerminationException();
        }
    }

    private void drawCardsInTurn(final BlackjackGame blackjackGame,
                                 final List<Participant> players, final Dealer dealer) {
        try {
            drawForMaximumCapability(blackjackGame, players);
            drawForMaximumCapability(blackjackGame, dealer);
        } catch (final EmptyDeckException e) {
            printGameClosing(e.getMessage());
            throw new GameTerminationException();
        }
    }

    private void drawForMaximumCapability(final BlackjackGame blackjackGame, final List<Participant> players) {
        for (final Participant player : players) {
            drawForMaximumCapability(blackjackGame, player);
        }
    }

    private void drawForMaximumCapability(final BlackjackGame blackjackGame, final Participant player) {
        while (player.isScoreLowerThanBlackJackValue() && isHitSelected(player)) {
            blackjackGame.distributeOneCard(player);
            OutputView.printPlayerHand(player);
        }
    }

    private boolean isHitSelected(final Participant player) {
        return InputView.takeHitOrStand(player.getNameValue());
    }

    private void drawForMaximumCapability(final BlackjackGame blackjackGame, final Dealer dealer) {
        while (dealer.needsAdditionalCard()) {
            blackjackGame.distributeOneCard(dealer);
            OutputView.printDealerAdditionalCardMessage();
        }
    }

    private void notifyFinalSummary(final BlackjackGame blackjackGame, final List<Participant> players, final Dealer dealer) {
        OutputView.printFinalHandsSummary(dealer, players);
        OutputView.printFinalOutcomeSummary(blackjackGame.judgeOutcome(), dealer.getNameValue());
    }
}
