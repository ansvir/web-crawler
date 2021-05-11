package com.project.webcrawler.initiliaze;

import com.project.webcrawler.domain.Hit;
import com.project.webcrawler.domain.Link;
import com.project.webcrawler.repository.HitRepository;
import com.project.webcrawler.repository.LinkRepository;
import com.project.webcrawler.repository.QueryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements ApplicationRunner {

    @Autowired
    private QueryRepository queryRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private HitRepository hitRepository;

    public void run(ApplicationArguments args) {
        Hit hit = new Hit(100);
        Hit hit2 = new Hit(200);
        Hit hit3 = new Hit(0);
        Hit hit4 = new Hit(300);

        Hit hit5 = new Hit(200);
        Hit hit6 = new Hit(20);
        Hit hit7 = new Hit(40);
        Hit hit8 = new Hit(260);

        Hit hit9 = new Hit(100);
        Hit hit10 = new Hit(50);
        Hit hit11 = new Hit(45);
        Hit hit12 = new Hit();

        Hit hit13 = new Hit();
        Hit hit14 = new Hit();
        Hit hit15 = new Hit();
        Hit hit16 = new Hit();

        Hit hit17 = new Hit();
        Hit hit18 = new Hit();
        Hit hit19 = new Hit();
        Hit hit20 = new Hit();

        Hit hit21 = new Hit();
        Hit hit22 = new Hit();
        Hit hit23 = new Hit();
        Hit hit24 = new Hit();

        Hit hit25 = new Hit();
        Hit hit26 = new Hit();
        Hit hit27 = new Hit();
        Hit hit28 = new Hit();

        Hit hit29 = new Hit();
        Hit hit30 = new Hit();
        Hit hit31 = new Hit();
        Hit hit32 = new Hit();

        Hit hit33 = new Hit();
        Hit hit34 = new Hit();
        Hit hit35 = new Hit();
        Hit hit36 = new Hit();

        Hit hit37 = new Hit();
        Hit hit38 = new Hit();
        Hit hit39 = new Hit();
        Hit hit40 = new Hit();

        Hit hit41 = new Hit();
        Hit hit42 = new Hit();
        Hit hit43 = new Hit();
        Hit hit44 = new Hit();

        Hit hit45 = new Hit();
        Hit hit46 = new Hit();
        Hit hit47 = new Hit();
        Hit hit48 = new Hit();

        Hit hit49 = new Hit();
        Hit hit50 = new Hit();
        Hit hit51 = new Hit();
        Hit hit52 = new Hit();

        Hit hit53 = new Hit();
        Hit hit54 = new Hit();
        Hit hit55 = new Hit();
        Hit hit56 = new Hit();

        Hit hit57 = new Hit();
        Hit hit58 = new Hit();
        Hit hit59 = new Hit();
        Hit hit60 = new Hit();

        Link link = new Link();
        Link link1 = new Link();
        Link link2 = new Link();
        Link link3 = new Link();
        Link link4 = new Link();
        Link link5 = new Link();
        Link link6 = new Link();
        Link link7 = new Link();
        Link link8 = new Link();
        Link link9 = new Link();
        Link link10 = new Link();
        Link link11 = new Link();
        Link link12 = new Link();
        Link link13 = new Link();
        Link link14 = new Link();
        Link link15 = new Link();

    }
}
