package demo.kang;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 **/
public class SimulateInternetApplication {

    /**
     * 测试入口
     * @param args
     */
    public static void main(String[] args) {
        //创建网络对线
        Internet internet = new Internet();
        //创建名为alice 和 bob 的电脑
        Computer alice = new Computer("alice");
        Computer bob = new Computer("bob");
        //alice 和 bob 上网
        alice.goInternet(internet);
        bob.goInternet(internet);
        //alice发送消息
        alice.sendMessage("hello,bob","bob");
        //网络工作
        internet.work();
        //bob读消息
        bob.readMessage();
    }

}

/**
 * 网络
 */
class Internet{
    private Set<Computer> computers ;

    public Internet() {
        computers = new HashSet<>();
    }

    /**
     * 电脑上网
     * @param computers
     */
    public void addComputer(Computer... computers){
        this.computers.addAll(Arrays.asList(computers));
    }

    /**
     * 网络开始工作
     */
    public void work(){
        //网络中所有的待发送消息
        Map<String,List<Message>> messages = computers.stream().flatMap(e->e.packets.stream().filter(i->i.getFrom().equals(e.name))).collect(Collectors.groupingBy(Message::getTo));
        computers.forEach(e->{
            List<Message> messageList = messages.get(e.name);
            if(messageList!=null){
                e.packets.addAll(messageList);
            }
        });
    }
}

/**
 * 电脑
 */
class Computer{
    public String name;
    public ArrayList<Message> packets;
    public Internet internet;
    public Computer(String name) {
        this.name = name;
        packets = new ArrayList<Message>(10);
    }

    /**
     * 上网
     * @param internet 网络
     * @return true 上网成功
     */
    public boolean goInternet(Internet internet){
        this.internet = internet;
        internet.addComputer(this);
        return true;
    }

    /**
     * 发送消息
     * @param message 消息内容
     * @param name 目的电脑名称
     */
    public void sendMessage(String message,String name){
        if(packets.size()>0){
            throw new RuntimeException("packets have not empty space!");
        }
        Message m = new Message(this.name,name,message);
        packets.add(m);
        System.out.println(this.name+" sendMessage "+m.toString());
    }
    public void sendMessage(Message message){
        if(packets.size()>0){
            throw new RuntimeException("packets have not empty space!");
        }
        packets.add(message);
    }

    /**
     * 读取别的电脑发过来的消息
     * @return 消息内容
     */
    public String readMessage(){
        if(packets.size()<1){
            throw new RuntimeException("packets is empty!");
        }
        Message m = packets.get(0);
        String message = m.getMessage();
        packets.remove(0);
        System.out.println(name+" readMessage "+m.toString());
        return message;
    }
}

/**
 * 网络消息
 * 包含 发送人 接收人 消息内容
 */
class Message{
    private String from;
    private String to;
    private String message;

    public Message(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
