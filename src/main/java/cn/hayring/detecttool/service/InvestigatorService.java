package cn.hayring.detecttool.service;

import cn.hayring.detecttool.dao.InvestigatorMapperDao;
import cn.hayring.detecttool.domain.Investigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvestigatorService implements UserDetailsService {

    InvestigatorMapperDao investigatorDao;





    @Autowired
    public void setInvestigatorDao(InvestigatorMapperDao investigatorMapperDao) {
        this.investigatorDao = investigatorMapperDao;
    }

    private GrantedAuthority DEFAULT_ROLE = new SimpleGrantedAuthority("INVESTIGATOR");



    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        // 1. 查询用户
        Investigator investigator = investigatorDao.selectById(id);
        if (investigator == null) {
            //log.warn("User: {} not found", login);
            throw new UsernameNotFoundException("Investigator " + id + " was not found in db");
        }
        // 2. 设置角色
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(DEFAULT_ROLE);

        return new org.springframework.security.core.userdetails.User(id,
                investigator.get_password(), grantedAuthorities);
    }
}
