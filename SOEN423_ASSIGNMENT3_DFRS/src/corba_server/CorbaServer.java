package corba_server;

import java.util.HashMap;
import java.util.Map.Entry;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import server.DistributedServer;
import server.FlightServerImpl;

public class CorbaServer {

	public static void main(String[] args) {
		DistributedServer server = new DistributedServer();
		HashMap<String, FlightServerImpl> flightServers = server.init();
		
		ORB orb = ORB.init(args, null);
		POA rootPOA;
		try {
			// Initialize RootPOA & Naming Service
			rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			org.omg.CORBA.Object nameServiceRef = orb.resolve_initial_references("NameService");
			NamingContextExt namingContextRef = NamingContextExtHelper.narrow(nameServiceRef);
			
			// Initialize Servers
			for(Entry<String, FlightServerImpl> entry : flightServers.entrySet()){
				FlightServerImpl flightServer = entry.getValue();
				org.omg.CORBA.Object corba_ref = rootPOA.servant_to_reference(flightServer);
				FlightReservationServer java_ref = FlightReservationServerHelper.narrow(corba_ref);
				String key = entry.getKey();
				NameComponent pathMtl[] = namingContextRef.to_name(key);
				namingContextRef.rebind(pathMtl, java_ref);
			}
		} catch (org.omg.CORBA.ORBPackage.InvalidName e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (AdapterInactive e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServantNotActive e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongPolicy e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotProceed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
