package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import gui.UserInterface;
import servico.ServicoListaInterface;

public class Cliente {
	
	public long id;

	public static void main(String[] args) {
		if (args.length == 2) {
			try {
				Cliente cli = new Cliente();
				ServicoListaInterface servicoLista = (ServicoListaInterface) Naming.lookup("//"+args[0]+"/"+args[1]);
				
				cli.id = System.currentTimeMillis();
				servicoLista.setIdCliente(cli.id);
				
				UserInterface gui = new UserInterface(servicoLista);
				gui.interfaceCliente(cli.id);
			} catch (MalformedURLException malfException) {
				System.out.println("\nMalformedURLException: " + malfException.toString());
			}  catch (RemoteException remException) {
				System.out.println("\nRemoteException: " + remException.toString());
			}  catch (NotBoundException notBoundException) {
				System.out.println("\nNotBoundException : " + notBoundException.toString());
			}  catch (Exception e) {
				System.out.println("\nException : " + e.toString());
			}
		} else {
			System.err.println("\nUse:\t java Cliente endereco objname\n");
			System.exit(0);
		}			
	}
}
