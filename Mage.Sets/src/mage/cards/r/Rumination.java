/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.r;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.game.Game;
import mage.players.Player;
import mage.target.common.TargetCardInHand;

/**
 *
 * @author Styxo
 */
public class Rumination extends CardImpl {

    public Rumination(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.SORCERY},"{2}{U}");

        // Draw three cards, then put a card from your hand on top of your library.
        this.getSpellAbility().addEffect(new RuminationEffect());
    }

    public Rumination(final Rumination card) {
        super(card);
    }

    @Override
    public Rumination copy() {
        return new Rumination(this);
    }

    class RuminationEffect extends OneShotEffect {

        public RuminationEffect() {
            super(Outcome.DrawCard);
            staticText = "Draw three cards, then put a card from your hand on top of your library.";
        }

        public RuminationEffect(final RuminationEffect effect) {
            super(effect);
        }

        @Override
        public RuminationEffect copy() {
            return new RuminationEffect(this);
        }

        @Override
        public boolean apply(Game game, Ability source) {
            Player player = game.getPlayer(source.getControllerId());
            if (player != null) {
                player.drawCards(3, game);
                putOnLibrary(player, source, game);
                return true;
            }
            return false;
        }

        private boolean putOnLibrary(Player player, Ability source, Game game) {
            TargetCardInHand target = new TargetCardInHand();
            if (target.canChoose(source.getSourceId(), player.getId(), game)) {
                player.chooseTarget(Outcome.ReturnToHand, target, source, game);
                Card card = player.getHand().get(target.getFirstTarget(), game);
                if (card != null) {
                    return player.moveCardToLibraryWithInfo(card, source.getSourceId(), game, Zone.HAND, true, false);
                }
            }
            return false;
        }
    }
}
