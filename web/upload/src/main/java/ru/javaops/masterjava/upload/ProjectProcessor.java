package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.util.*;

@Slf4j
public class ProjectProcessor {
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private final JaxbParser parser = new JaxbParser(ObjectFactory.class);

    public Map<String, Group> getGroups(StaxStreamProcessor processor) throws XMLStreamException, JAXBException {
        final Map<String, Project> projects = projectDao.getAsMap();
        final Map<String, Group> groups = groupDao.getAsMap();

        while (processor.startElement("Project", "Projects")) {
            final JaxbUnmarshaller unmarshaller = parser.createUnmarshaller();

            final ru.javaops.masterjava.xml.schema.Project projectElement = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.Project.class);
            Project project = projects.get(projectElement.getName());

            if (!projects.containsKey(projectElement.getName())) {
                project = new Project(projectElement.getName(), projectElement.getDescription());
                log.info("Insert project : <{}>.", project);
                projectDao.insert(project);
            }

            Project finalProject = project;
            projectElement.getGroup().forEach(
                    g -> {
                        final Group group = new Group(g.getName(), GroupType.valueOf(g.getType().name()), finalProject.getId());

                        if (!groups.containsKey(g.getName())) {
                            log.info("Insert group : <{}>.", group);
                            groupDao.insert(group);
                        } else {
                            group.setId(groups.get(group.getName()).getId());
                            log.info("Update group : <{}>.", group);
                            groupDao.update(group);
                        }
                    }
            );
        }
        return groupDao.getAsMap();
    }
}
