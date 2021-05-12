package com.project.webcrawler.initiliaze;

import com.project.webcrawler.domain.Hit;
import com.project.webcrawler.domain.Link;
import com.project.webcrawler.domain.Query;
import com.project.webcrawler.repository.HitRepository;
import com.project.webcrawler.repository.LinkRepository;
import com.project.webcrawler.repository.QueryRepository;
import lombok.AllArgsConstructor;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class DatabaseLoader implements ApplicationRunner {

    @Autowired
    private QueryRepository queryRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private HitRepository hitRepository;

    public void run(ApplicationArguments args) {

        //test data
        Hit hit = new Hit(100);
        Hit hit2 = new Hit(200);
        Hit hit3 = new Hit(0);
        Hit hit4 = new Hit(150);

        List<Hit> hits1 = new LinkedList<>();
        hits1.add(hit);hits1.add(hit2);hits1.add(hit3);hits1.add(hit4);

        hitRepository.saveAll(hits1);

        Hit hit5 = new Hit(200);
        Hit hit6 = new Hit(20);
        Hit hit7 = new Hit(40);
        Hit hit8 = new Hit(40);

        List<Hit> hits2 = new LinkedList<>();
        hits2.add(hit5);hits2.add(hit6);hits2.add(hit7);hits2.add(hit8);

        hitRepository.saveAll(hits2);

        Hit hit9 = new Hit(100);
        Hit hit10 = new Hit(50);
        Hit hit11 = new Hit(45);
        Hit hit12 = new Hit(0);

        List<Hit> hits3 = new LinkedList<>();
        hits3.add(hit9);hits3.add(hit10);hits3.add(hit11);hits3.add(hit12);

        hitRepository.saveAll(hits3);

        Hit hit13 = new Hit(20);
        Hit hit14 = new Hit(45);
        Hit hit15 = new Hit(50);
        Hit hit16 = new Hit(10);

        List<Hit> hits4 = new LinkedList<>();
        hits4.add(hit13);hits4.add(hit14);hits4.add(hit15);hits4.add(hit16);

        hitRepository.saveAll(hits4);

        Hit hit17 = new Hit(50);
        Hit hit18 = new Hit(55);
        Hit hit19 = new Hit(11);
        Hit hit20 = new Hit(14);

        List<Hit> hits5 = new LinkedList<>();
        hits5.add(hit17);hits5.add(hit18);hits5.add(hit19);hits5.add(hit20);

        hitRepository.saveAll(hits5);

        Hit hit21 = new Hit(15);
        Hit hit22 = new Hit(17);
        Hit hit23 = new Hit(20);
        Hit hit24 = new Hit(40);

        List<Hit> hits6 = new LinkedList<>();
        hits6.add(hit21);hits6.add(hit22);hits6.add(hit23);hits6.add(hit24);

        hitRepository.saveAll(hits6);

        Hit hit25 = new Hit(100);
        Hit hit26 = new Hit(110);
        Hit hit27 = new Hit(20);
        Hit hit28 = new Hit(45);

        List<Hit> hits7 = new LinkedList<>();
        hits7.add(hit25);hits7.add(hit26);hits7.add(hit27);hits7.add(hit28);

        hitRepository.saveAll(hits7);

        Hit hit29 = new Hit(24);
        Hit hit30 = new Hit(55);
        Hit hit31 = new Hit(78);
        Hit hit32 = new Hit(12);

        List<Hit> hits8 = new LinkedList<>();
        hits8.add(hit29);hits8.add(hit30);hits8.add(hit31);hits8.add(hit32);

        hitRepository.saveAll(hits8);

        Hit hit33 = new Hit(0);
        Hit hit34 = new Hit(14);
        Hit hit35 = new Hit(15);
        Hit hit36 = new Hit(18);

        List<Hit> hits9 = new LinkedList<>();
        hits9.add(hit33);hits9.add(hit34);hits9.add(hit35);hits9.add(hit36);

        hitRepository.saveAll(hits9);

        Hit hit37 = new Hit(20);
        Hit hit38 = new Hit(80);
        Hit hit39 = new Hit(55);
        Hit hit40 = new Hit(10);

        List<Hit> hits10 = new LinkedList<>();
        hits10.add(hit37);hits10.add(hit38);hits10.add(hit39);hits10.add(hit40);

        hitRepository.saveAll(hits10);

        Hit hit41 = new Hit(20);
        Hit hit42 = new Hit(40);
        Hit hit43 = new Hit(50);
        Hit hit44 = new Hit(40);

        List<Hit> hits11 = new LinkedList<>();
        hits11.add(hit41);hits11.add(hit42);hits11.add(hit43);hits11.add(hit44);

        hitRepository.saveAll(hits11);

        Hit hit45 = new Hit(0);
        Hit hit46 = new Hit(50);
        Hit hit47 = new Hit(200);
        Hit hit48 = new Hit(100);

        List<Hit> hits12 = new LinkedList<>();
        hits12.add(hit45);hits12.add(hit46);hits12.add(hit47);hits12.add(hit48);

        hitRepository.saveAll(hits12);

        Hit hit49 = new Hit(400);
        Hit hit50 = new Hit(40);
        Hit hit51 = new Hit(20);
        Hit hit52 = new Hit(10);

        List<Hit> hits13 = new LinkedList<>();
        hits13.add(hit49);hits13.add(hit50);hits13.add(hit51);hits13.add(hit52);

        hitRepository.saveAll(hits13);

        Hit hit53 = new Hit(40);
        Hit hit54 = new Hit(10);
        Hit hit55 = new Hit(40);
        Hit hit56 = new Hit(20);

        List<Hit> hits14 = new LinkedList<>();
        hits14.add(hit53);hits14.add(hit54);hits14.add(hit55);hits14.add(hit56);

        hitRepository.saveAll(hits14);

        Hit hit57 = new Hit(20);
        Hit hit58 = new Hit(10);
        Hit hit59 = new Hit(20);
        Hit hit60 = new Hit(0);

        List<Hit> hits15 = new LinkedList<>();
        hits15.add(hit57);hits15.add(hit58);hits15.add(hit59);hits15.add(hit60);

        hitRepository.saveAll(hits15);

        Link link1 = new Link("link1");
        link1.setHits(hits1);
        Link link2 = new Link("link2");
        link2.setHits(hits2);
        Link link3 = new Link("link3");
        link3.setHits(hits3);
        Link link4 = new Link("link4");
        link4.setHits(hits4);
        Link link5 = new Link("link5");
        link5.setHits(hits5);
        Link link6 = new Link("link6");
        link6.setHits(hits6);
        Link link7 = new Link("link7");
        link7.setHits(hits7);
        Link link8 = new Link("link8");
        link8.setHits(hits8);
        Link link9 = new Link("link9");
        link9.setHits(hits9);
        Link link10 = new Link("link10");
        link10.setHits(hits10);
        Link link11 = new Link("link11");
        link11.setHits(hits11);
        Link link12 = new Link("link12");
        link12.setHits(hits12);
        Link link13 = new Link("link13");
        link13.setHits(hits13);
        Link link14 = new Link("link14");
        link14.setHits(hits14);
        Link link15 = new Link("link15");
        link15.setHits(hits15);

        List<Link> links = new ArrayList<>();

        links.add(link1);links.add(link2);links.add(link3);links.add(link4);links.add(link5);links.add(link6);
        links.add(link7);links.add(link8);links.add(link9);links.add(link10);links.add(link11);links.add(link12);
        links.add(link13);links.add(link14);links.add(link15);

        linkRepository.saveAll(links);

        Query query = new Query();
        query.setLinks(links);

        Query savedQuery = queryRepository.save(query);
        DateTimeFormatter dateFormat = DateTimeFormat
                .forPattern("d-M-Y H:m:s");
        savedQuery.setName("Query" + savedQuery.getId() + " " + dateFormat.print(new LocalDateTime()));
        queryRepository.save(savedQuery);

    }
}
