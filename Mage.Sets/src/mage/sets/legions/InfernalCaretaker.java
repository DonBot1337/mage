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
package mage.sets.legions;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.TurnedFaceUpSourceTriggeredAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.keyword.MorphAbility;
import mage.cards.CardImpl;
import mage.cards.Cards;
import mage.cards.CardsImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.filter.FilterCard;
import mage.filter.common.FilterBySubtypeCard;
import mage.game.Game;
import mage.players.Player;

/**
 *
 * @author cg5
 */
public class InfernalCaretaker extends CardImpl {

    public InfernalCaretaker(UUID ownerId) {
        super(ownerId, 76, "Infernal Caretaker", Rarity.COMMON, new CardType[]{CardType.CREATURE}, "{3}{B}");
        this.expansionSetCode = "LGN";
        this.subtype.add("Human");
        this.subtype.add("Cleric");
        this.power = new MageInt(2);
        this.toughness = new MageInt(2);

        // Morph {3}{B}
        this.addAbility(new MorphAbility(this, new ManaCostsImpl("{3}{B}")));
        
        // When Infernal Caretaker is turned face up, return all Zombie cards from all graveyards to their owners' hands.
        this.addAbility(new TurnedFaceUpSourceTriggeredAbility(new InfernalCaretakerEffect()));
    }

    public InfernalCaretaker(final InfernalCaretaker card) {
        super(card);
    }

    @Override
    public InfernalCaretaker copy() {
        return new InfernalCaretaker(this);
    }
}

class InfernalCaretakerEffect extends OneShotEffect {
    
    private static FilterCard zombieCard = new FilterBySubtypeCard("Zombie");
    
    public InfernalCaretakerEffect() {
        super(Outcome.ReturnToHand);
        this.staticText = "return all Zombie cards from all graveyards to their owners' hands";
    }
    
    public InfernalCaretakerEffect(final InfernalCaretakerEffect effect) {
        super(effect);
    }
    
    @Override
    public InfernalCaretakerEffect copy() {
        return new InfernalCaretakerEffect(this);
    }
    
    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            Cards toHands = new CardsImpl();
            for (UUID playerId : game.getState().getPlayersInRange(controller.getId(), game)) {
                Player player = game.getPlayer(playerId);
                if (player != null) {
                    toHands.addAll(player.getGraveyard().getCards(zombieCard, source.getSourceId(), controller.getId(), game));
                }
            }
            controller.moveCards(toHands.getCards(game), Zone.HAND, source, game);
            return true;
        }
        return false;
    }
}
