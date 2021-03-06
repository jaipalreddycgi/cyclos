/*
 *
 *    This file is part of Cyclos.
 *
 *    Cyclos is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    Cyclos is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Cyclos; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *
 */

package nl.strohalm.cyclos.controls.tokens;

import nl.strohalm.cyclos.annotations.Inject;
import nl.strohalm.cyclos.controls.ActionContext;
import nl.strohalm.cyclos.controls.BaseFormAction;
import nl.strohalm.cyclos.dao.tokens.exceptions.TokenNotFoundException;
import nl.strohalm.cyclos.entities.access.Channel;
import nl.strohalm.cyclos.entities.access.PrincipalType;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.entities.settings.LocalSettings;
import nl.strohalm.cyclos.entities.settings.events.LocalSettingsChangeListener;
import nl.strohalm.cyclos.entities.settings.events.LocalSettingsEvent;
import nl.strohalm.cyclos.services.elements.ElementService;
import nl.strohalm.cyclos.services.settings.SettingsService;
import nl.strohalm.cyclos.services.tokens.GenerateTokenDTO;
import nl.strohalm.cyclos.services.tokens.TokenService;
import nl.strohalm.cyclos.services.tokens.exceptions.BadStatusForRedeem;
import nl.strohalm.cyclos.services.tokens.exceptions.InvalidPinException;
import nl.strohalm.cyclos.services.tokens.exceptions.NoTransferTypeException;
import nl.strohalm.cyclos.services.tokens.exceptions.RefundNonExpiredToken;
import nl.strohalm.cyclos.services.transactions.exceptions.NotEnoughCreditsException;
import nl.strohalm.cyclos.services.transfertypes.TransactionFeeService;
import nl.strohalm.cyclos.services.transfertypes.TransferTypeService;
import nl.strohalm.cyclos.utils.SettingsHelper;
import nl.strohalm.cyclos.utils.binding.BeanBinder;
import nl.strohalm.cyclos.utils.binding.DataBinder;
import nl.strohalm.cyclos.utils.binding.PropertyBinder;
import org.apache.struts.action.ActionForward;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class BaseTokenAction<T> extends BaseFormAction implements LocalSettingsChangeListener {


    private DataBinder<T> dataBinder;

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);

    TokenService tokenService;

    @Inject
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    SettingsService settingsService;

    @Inject
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    ElementService elementService;

    @Inject
    public void setElementService(ElementService elementService) {
        this.elementService = elementService;
    }

    TransactionFeeService transactionFeeService;

    TransferTypeService transferTypeService;

    @Inject
    public void setTransactionFeeService(TransactionFeeService transactionFeeService) {
        this.transactionFeeService = transactionFeeService;
    }

    @Inject
    public void setTransferTypeService(TransferTypeService transferTypeService) {
        this.transferTypeService = transferTypeService;
    }

    Member loadLoggedMember(ActionContext actionContext) {
        return elementService.loadByPrincipal(new PrincipalType(Channel.Principal.USER), actionContext.getUser().getUsername());
    }

    @Override
    protected final ActionForward handleSubmit(ActionContext context) throws Exception {
        String errorKey;
        BaseTokenForm baseTokenForm = context.getForm();
        try {
            Member member = loadLoggedMember(context);
            return tokenSubmit(baseTokenForm, member, context);
        } catch (TokenNotFoundException e) {
            errorKey = "tokens.error.tokenNotFound";
        } catch (BadStatusForRedeem e) {
            errorKey = "tokens.error.badStatusForRedeem";
        } catch (NoTransferTypeException e) {
            errorKey = "tokens.error.noTransferType";
        } catch (RefundNonExpiredToken e) {
            errorKey = "tokens.error.refundNotExpired";
        } catch (NotEnoughCreditsException e) {
            errorKey = "tokens.error.notEnoughCredits";
        } catch (InvalidPinException e) {
            errorKey = "tokens.error.invalidPin";
        }

        return context.sendError(errorKey, baseTokenForm.getTokenId());
    }

    abstract ActionForward tokenSubmit(BaseTokenForm token, Member loggedMember, ActionContext actionContext) throws Exception;


    public DataBinder<T> getDataBinder() {
        try {
            lock.writeLock().lock();
            if (dataBinder == null) {
                final LocalSettings localSettings = SettingsHelper.getLocalSettings(getServlet().getServletContext());
                dataBinder = createBinder(localSettings);
            }
        } finally {
            lock.writeLock().unlock();
        }
        return dataBinder;
    }

    protected DataBinder createBinder(LocalSettings localSettings) {
        return null;
    };

    @Override
    public void onLocalSettingsUpdate(LocalSettingsEvent event) {
        try {
            lock.writeLock().lock();
            dataBinder = null;
        } finally {
            lock.writeLock().unlock();
        }
    }
}

