package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import gui.UserInterface;
import servico.ServicoListaInterface;

public class Cliente {
	
	public long id;

	public static void main(String[] args) {
		if (args.length == 0) {
			try {
				String ipServidor = null;
				String op = JOptionPane.showInputDialog(null, "Escolha o servidor: \n1) Servidor 1\n2)Servidor 2"); 
				if (op.equals("1"))
					ipServidor = "127.0.0.1";
				else
					ipServidor = "192.168.15.7";
				Cliente cli = new Cliente();
				ServicoListaInterface servicoLista = (ServicoListaInterface) Naming.lookup("//"+ipServidor+"/"+"servico");
				
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
