package servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import servico.ServicoLista;

class Servidor {
	public static void main(String[] args) {
		
		if (args.length < 3) {
			try {
				ServicoLista servico = new ServicoLista();
				Naming.rebind("//" + args[0] + "/"+ args[1], servico);
				
			} catch (MalformedURLException malformedExcep) {
				System.out.println("Erro: " + malformedExcep.toString());
				System.exit(0);
			} catch (RemoteException remoteExcep) {
				System.out.println("Erro:" + remoteExcep.toString());
				System.exit(1);
			} catch (Exception e) {
				System.out.println("Erro: " + e.toString());
				System.exit(3);
			}			
		} else {			
			System.err.println("\nUse:\t java Server objname\n");
			System.err.println("Use o comando javac Server objname");
			System.err.println("Pois o objeto está passado por parâmetro para inicializar o servidor.");
			System.exit(4);
		}
		
		
		System.out.println("Servidor no Ar!");
	}

}
