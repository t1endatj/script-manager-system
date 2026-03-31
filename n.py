import os

def generate_java_files():
    base_path = "src/main/java/scriptmanager"
    
    entities = [
        {"name": "SuKienTiec", "pkg": "core"},
        {"name": "HangMucKichBan", "pkg": "core"}
    ]

    templates = {
        "dao_interface": """package scriptmanager.dao;
import scriptmanager.entity.core.{entity};
public interface {entity}Dao extends GenericDao<{entity}, Integer> {{
}}""",

        "dao_impl": """package scriptmanager.dao;
import scriptmanager.entity.core.{entity};
public class {entity}DaoImpl extends GenericDaoImpl<{entity}, Integer> implements {entity}Dao {{
    public {entity}DaoImpl() {{
        super({entity}.class);
    }}
}}""",

        "service_interface": """package scriptmanager.service;
import scriptmanager.entity.core.{entity};
import java.util.List;
public interface {entity}Service {{
    List<{entity}> findAll();
    {entity} findById(int id);
    void save({entity} item);
    void update({entity} item);
    void delete(int id);
}}""",

        "service_impl": """package scriptmanager.service;
import scriptmanager.dao.{entity}Dao;
import scriptmanager.dao.{entity}DaoImpl;
import scriptmanager.entity.core.{entity};
import java.util.List;
public class {entity}ServiceImpl implements {entity}Service {{
    private final {entity}Dao dao;
    public {entity}ServiceImpl() {{
        this.dao = new {entity}DaoImpl();
    }}
    @Override
    public List<{entity}> findAll() {{ return dao.findAll(); }}
    @Override
    public {entity} findById(int id) {{ return dao.findById(id); }}
    @Override
    public void save({entity} item) {{ dao.save(item); }}
    @Override
    public void update({entity} item) {{ dao.update(item); }}
    @Override
    public void delete(int id) {{
        {entity} item = dao.findById(id);
        if (item != null) dao.delete(item);
    }}
}}"""
    }

    for entity in entities:
        name = entity["name"]
        
        # Tạo DAO
        write_file(f"{base_path}/dao/{name}Dao.java", templates["dao_interface"].format(entity=name))
        write_file(f"{base_path}/dao/{name}DaoImpl.java", templates["dao_impl"].format(entity=name))
        
        # Tạo Service
        write_file(f"{base_path}/service/{name}Service.java", templates["service_interface"].format(entity=name))
        write_file(f"{base_path}/service/{name}ServiceImpl.java", templates["service_impl"].format(entity=name))

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)
    print(f"Created: {path}")

if __name__ == "__main__":
    generate_java_files()