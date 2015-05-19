package fr.sii.service.admin.comment;

import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.repository.admin.comment.AdminCommentRepository;
import fr.sii.service.admin.user.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 29/04/2015.
 */
@Service
public class AdminCommentService {

    @Autowired
    private AdminCommentRepository adminCommentRepository;

    @Autowired
    private AdminUserService adminUserService;

    public List<AdminComment> matchUsers(List<AdminComment> rs)
    {
        List<AdminComment> nrs = new ArrayList<>();
        for (AdminComment r : rs)
        {
            AdminUser u = adminUserService.findOne(r.getUserId());
            r.setUser(u);
            nrs.add(r);
        }
        return nrs;
    }
    public AdminComment matchUser(List<AdminComment> rs)
    {
        List<AdminComment> nrs = new ArrayList<>();
        if(rs.size() > 0)
        {
            AdminComment r = rs.get(0);
            AdminUser u = adminUserService.findOne(r.getUserId());
            r.setUser(u);
            return r;
        }
        return null;
    }

    public AdminComment matchUser(AdminComment r)
    {
        if(r != null)
        {
            AdminUser u = adminUserService.findOne(r.getUserId());
            r.setUser(u);
            return r;
        }
        return null;
    }

    public List<AdminComment> findAll()
    {
        return matchUsers(adminCommentRepository.findAll());
    }

    public void deleteAll()
    {
        adminCommentRepository.deleteAll();
    }

    public AdminComment save(AdminComment r)
    {
        if(r.getAdded() == null)
        {
            r.setAdded(new Date());
        }
        AdminComment s = adminCommentRepository.save(r);
        return matchUser(s);
    }

    public AdminComment put(Long id,AdminComment r)
    {
        AdminComment pr = findOne(id);
        if(pr != null)
        {
            if(r.getAdded() == null)
            {
                r.setAdded(new Date());
            }
            adminCommentRepository._delete(id);
            r.setEntityId(id);
            return matchUser(adminCommentRepository.save(r));
        }
        return null;
    }

    public void delete(Long id)
    {
        adminCommentRepository._delete(id);
    }

    public AdminComment findOne(Long id)
    {
        List<AdminComment> rs = adminCommentRepository.findByEntityId(id);
        return matchUser(rs);
    }

    public List<AdminComment> findByUserId(Long id)
    {
        return matchUsers(adminCommentRepository.findByUserId(id));
    }

    public List<AdminComment> findByRowId(Long rowId)
    {
        return matchUsers(adminCommentRepository.findByRowIdOrderByAddedAsc(rowId));
    }
}