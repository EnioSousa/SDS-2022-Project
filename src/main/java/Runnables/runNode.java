package Runnables;

import PeerToPeer.Node;
import PeerToPeer.NodeInfo;
import Utils.Bootstrap;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Scanner;

public class runNode {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {

        int i;
        Scanner scan = new Scanner(System.in);

        Node node = new Node(new NodeInfo(new byte[]{0x02}, args[0], Integer.parseInt(args[1])));

        menu();
        i = scan.nextInt();
        while (i != 0){
            if(i == 1){
                node.doJoin(new NodeInfo(Bootstrap.bootstrapId, Bootstrap.bootstrapIp, Bootstrap.bootstrapPort));
            }
            else if(i == 2){
                System.out.println("Enter the key and value:");
                String key = scan.next();
                String value = scan.next();
                node.doStore(key.getBytes(StandardCharsets.UTF_8),value.getBytes(StandardCharsets.UTF_8));
            }
            else if(i == 3){
                System.out.println("Enter the key to find:");
                String key = scan.next();
                node.doFind(key.getBytes(StandardCharsets.UTF_8));
            }
            else if(i == 0){
                System.out.println("Bye");
                return;
            }
            menu();
            i = scan.nextInt();
        }

        return;

    }

    public static void menu(){
        System.out.println("1- Do join to enter the network");
        System.out.println("2- Do store to a node");
        System.out.println("3- Find value from a node");
        System.out.println("0- Quit");
    }
}
