package sustain.geosieve.create;

import net.sourceforge.argparse4j.inf.Namespace;

public abstract class JobLauncher {
    protected final Namespace params;
    protected final FilterDatabase filters;
    protected final GisJoinMapper mapper;

    public JobLauncher(Namespace params) {
        this.params = params;
        this.filters = Factories.getFilters(params);
        this.mapper = Factories.getMapper(params);
    }

    public abstract void launch();
}
