package ninja.onewaysidewalks.fantasyurf.stats.calculator.worker.resources;

import ninja.onewaysidewalks.fantasyurf.stats.calculator.worker.StatCalculatorWorker;

import javax.inject.Inject;

public class AdminResourceImpl implements AdminResource {

    private final StatCalculatorWorker statCalculatorWorker;

    @Inject
    public AdminResourceImpl(StatCalculatorWorker statCalculatorWorker) {
        this.statCalculatorWorker = statCalculatorWorker;
    }

    @Override
    public void calculate(CalculateRequest request) {
        statCalculatorWorker.handleMessage(request.getTimeBucket());
    }
}
