package blakcjack.controller;

import blakcjack.domain.blackjackgame.BlackjackGame;
import blakcjack.domain.card.Deck;
import blakcjack.domain.participant.Dealer;
import blakcjack.domain.participant.Participant;
import blakcjack.view.InputView;
import blakcjack.view.OutputView;

import java.util.List;

import static blakcjack.view.InputView.takePlayerNamesInput;
import static blakcjack.view.OutputView.printInitialHands;

public class BlackJackController {
    public void run() {
        final BlackjackGame blackjackGame = new BlackjackGame(new Deck(), takePlayerNamesInput());
        blackjackGame.initializeHands();

        final List<Participant> players = blackjackGame.getPlayers();
        final Dealer dealer = blackjackGame.getDealer();
        printInitialHands(dealer, players);

        drawForMaximumCapability(blackjackGame, players);
        drawForMaximumCapability(blackjackGame, dealer);
        printFinalSummary(blackjackGame, players, dealer);
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

    private void printFinalSummary(final BlackjackGame blackjackGame, final List<Participant> players, final Dealer dealer) {
        OutputView.printFinalHandsSummary(dealer, players);
        OutputView.printFinalOutcomeSummary(blackjackGame.judgeOutcome(), dealer.getNameValue());
    }
}
