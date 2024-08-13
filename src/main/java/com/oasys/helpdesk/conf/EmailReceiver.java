//package com.oasys.helpdesk.conf;
//
//import com.oasys.helpdesk.service.EmailTicketCreationService;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.channel.QueueChannel;
//import org.springframework.integration.config.EnableIntegration;
//import org.springframework.integration.dsl.IntegrationFlow;
//import org.springframework.integration.dsl.IntegrationFlows;
//import org.springframework.integration.dsl.Pollers;
//import org.springframework.integration.mail.dsl.Mail;
//import org.springframework.integration.mail.support.DefaultMailHeaderMapper;
//import org.springframework.integration.mapping.HeaderMapper;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.PollableChannel;
//import org.springframework.stereotype.Service;
//
//import javax.mail.internet.MimeMessage;
//
//@Log4j2
//@Configuration
//@EnableIntegration
//@Service
//public class EmailReceiver {
//
//    @Autowired
//    private PollableChannel pop3Channel;
//
//    @Autowired
//    private EmailTicketCreationService ticketCreationService;
//
//
//    @Bean
//    public PollableChannel receivedChannel() {
//        return new QueueChannel();
//    }
//
//    @Autowired
//    private EmailTicketCreationService emailTicketCreationService;
//
//    @Value("${spring.mail.username}")
//	private String mailid;
//
//    @Value("${spring.mail.password}")
//  	private String password;
//      
//
//    @Bean
//    public IntegrationFlow pop3MailFlow() {
//        return IntegrationFlows
//               // .from(Mail.pop3InboundAdapter("mail.upexciseonline.co", 995,mailid ,password)
//                		 .from(Mail.pop3InboundAdapter("mail.upexciseonline.co", 995,"no-replyhelpdesk@upexciseonline.co" ,"no-reply@oasys")               
//                		.javaMailProperties(p -> {
//                                    p.put("mail.debug", "false");
//                                    p.put("mail.pop3.socketFactory.fallback", "false");
//                                    p.put("mail.pop3.port", 995);
//                                    p.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//                                    p.put("mail.pop3.socketFactory.port", 995);
//                                })
//                                .maxFetchSize(1),
//                        e -> e.poller(Pollers.fixedRate(5000).maxMessagesPerPoll(1)))
//                .handle(ticketCreationService)
//                .get();
//    }
//
//    @Bean
//    public HeaderMapper<MimeMessage> mailHeaderMapper() {
//        return new DefaultMailHeaderMapper();
//    }
//
//    public void read() {
//        Message<?> message = this.pop3Channel.receive(10000);
//        System.out.println("Mesasge -"+ message.getPayload());
//    }
//
//}
