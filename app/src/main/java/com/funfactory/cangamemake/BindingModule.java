package com.funfactory.cangamemake;

import com.funfactory.cangamemake.db.CanGameDatabase;
import com.funfactory.cangamemake.db.IDBManager;
import com.funfactory.cangamemake.model.IConfigModel;
import com.funfactory.cangamemake.model.IPECSModel;
import com.funfactory.cangamemake.model.IPacienteModel;
import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.IRotinaModel;
import com.funfactory.cangamemake.model.dao.ICategoriaDAO;
import com.funfactory.cangamemake.model.dao.IConfigDAO;
import com.funfactory.cangamemake.model.dao.IPECSDAO;
import com.funfactory.cangamemake.model.dao.IPacienteDAO;
import com.funfactory.cangamemake.model.dao.IRankingDAO;
import com.funfactory.cangamemake.model.dao.IRotinaDAO;
import com.funfactory.cangamemake.model.dao.impl.CategoriaDAO;
import com.funfactory.cangamemake.model.dao.impl.ConfigDAO;
import com.funfactory.cangamemake.model.dao.impl.PECSDAO;
import com.funfactory.cangamemake.model.dao.impl.PacienteDAO;
import com.funfactory.cangamemake.model.dao.impl.RankingDAO;
import com.funfactory.cangamemake.model.dao.impl.RotinaDAO;
import com.funfactory.cangamemake.model.impl.ConfigModel;
import com.funfactory.cangamemake.model.impl.PECSModel;
import com.funfactory.cangamemake.model.impl.PacienteModel;
import com.funfactory.cangamemake.model.impl.RankingModel;
import com.funfactory.cangamemake.model.impl.RotinaModel;
import com.funfactory.cangamemake.presenter.IConfigPresenter;
import com.funfactory.cangamemake.presenter.IListarPECSPresenter;
import com.funfactory.cangamemake.presenter.IListarPacientePresenter;
import com.funfactory.cangamemake.presenter.IListarRotinaPresenter;
import com.funfactory.cangamemake.presenter.IPECSPresenter;
import com.funfactory.cangamemake.presenter.IPacientePresenter;
import com.funfactory.cangamemake.presenter.IRankingPresenter;
import com.funfactory.cangamemake.presenter.ISidebarPresenter;
import com.funfactory.cangamemake.presenter.impl.ConfigPresenter;
import com.funfactory.cangamemake.presenter.impl.ListarPECSPresenter;
import com.funfactory.cangamemake.presenter.impl.ListarPacientePresenter;
import com.funfactory.cangamemake.presenter.impl.ListarRotinaPresenter;
import com.funfactory.cangamemake.presenter.impl.PECSPresenter;
import com.funfactory.cangamemake.presenter.impl.PacientePresenter;
import com.funfactory.cangamemake.presenter.impl.RankingPresenter;
import com.funfactory.cangamemake.presenter.impl.SidebarPresenter;
import com.google.inject.AbstractModule;

public class BindingModule extends AbstractModule {

	@Override
	protected void configure() {

		// PROVIDER
//		this.bind(SpiceManager.class).toProvider(RoboSpiceProvider.class).in(Singleton.class);
//		this.bind(OfflineSpiceManager.class).toProvider(RoboSpiceOfflineProvider.class).in(Singleton.class);

		// MODEL
		this.bind(IPacienteModel.class).to(PacienteModel.class);
		this.bind(IConfigModel.class).to(ConfigModel.class);
		this.bind(IPECSModel.class).to(PECSModel.class);
		this.bind(IRotinaModel.class).to(RotinaModel.class);
		this.bind(IRankingModel.class).to(RankingModel.class);

		// PRESENTER
		this.bind(IPacientePresenter.class).to(PacientePresenter.class);
		this.bind(IListarPacientePresenter.class).to(ListarPacientePresenter.class);
		this.bind(ISidebarPresenter.class).to(SidebarPresenter.class);
		this.bind(IConfigPresenter.class).to(ConfigPresenter.class);
		this.bind(IListarPECSPresenter.class).to(ListarPECSPresenter.class);
		this.bind(IListarRotinaPresenter.class).to(ListarRotinaPresenter.class);
		this.bind(IRankingPresenter.class).to(RankingPresenter.class);
		this.bind(IPECSPresenter.class).to(PECSPresenter.class);

		//DAO
		this.bind(IPacienteDAO.class).to(PacienteDAO.class);
		this.bind(IConfigDAO.class).to(ConfigDAO.class);
		this.bind(IPECSDAO.class).to(PECSDAO.class);
		this.bind(IRotinaDAO.class).to(RotinaDAO.class);
		this.bind(IRankingDAO.class).to(RankingDAO.class);
		this.bind(ICategoriaDAO.class).to(CategoriaDAO.class);

		// DATABASE
		this.bind(IDBManager.class).to(CanGameDatabase.class);
//
//		// OTHER
//		this.bind(ISPODPreferences.class).to(SPODPreferences.class);
//		this.bind(ICommandFactory.class).to(CommandFactoryImpl.class);
		

		
	}
}