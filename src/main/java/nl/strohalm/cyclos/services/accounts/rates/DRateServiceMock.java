/*
   This file is part of Cyclos.

   Cyclos is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   Cyclos is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Cyclos; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 */
package nl.strohalm.cyclos.services.accounts.rates;

import java.math.BigDecimal;
import java.util.Calendar;

import nl.strohalm.cyclos.entities.accounts.Account;
import nl.strohalm.cyclos.entities.accounts.AccountStatus;
import nl.strohalm.cyclos.entities.accounts.Currency;
import nl.strohalm.cyclos.entities.accounts.transactions.Transfer;
import nl.strohalm.cyclos.services.stats.StatisticalResultDTO;
import nl.strohalm.cyclos.services.transactions.GrantLoanDTO;
import nl.strohalm.cyclos.services.transactions.TransferDTO;

public class DRateServiceMock implements DRateService {

    public void applyTransfer(final AccountStatus status, final Transfer transfer) {
    }

    public BigDecimal convertWithDRate(final BigDecimal amount, final Account fromAccount, final Calendar date) {
        return null;
    }

    public BigDecimal getActualRate(final Account account, final Calendar date) {
        return null;
    }

    public BigDecimal getActualRate(final AccountStatus status) {
        return null;
    }

    public BigDecimal getActualRate(final AccountStatus status, final Calendar date) {
        return null;
    }

    public BigDecimal getDRateConversionResult(final RatesDTO setOfUnits) {
        return null;
    }

    public StatisticalResultDTO getRateConfigGraph(final DRateConfigSimulationDTO input) {
        return null;
    }

    public Object getRateForTransferFrom(final Transfer transfer) {
        return null;
    }

    public AccountStatus handleInitializationOnRateBalanceCorrection(final AccountStatus status) {
        return status;
    }

    public AccountStatus initializeRateBalanceCorrectionOnAccounts(final AccountStatus status, final Transfer transfer) {
        return status;
    }

    public boolean isRated(final AccountStatus status) {
        return false;
    }

    public boolean isRated(final AccountStatus status, final Calendar date) {
        return false;
    }

    public RatesDTO merge(final RatesDTO params) {
        return null;
    }

    public Object merge(final RatesDTO setOfUnits1, final RatesDTO setOfUnits2) {
        return null;
    }

    public void setRateToLoanTransfer(final TransferDTO dto, final GrantLoanDTO params, final Currency currency) {
    }

    public BigDecimal updateRateBalanceCorrectionOnFromAccount(final AccountStatus status, final BigDecimal amount) {
        return null;
    }

    public void validate(final DRateConfigSimulationDTO dto) {
    }

}
