const API_BASE = '/api/tasks';
const PROJECTS_API = '/api/projects';

let editingTaskId = null;
let editingProjectId = null;
let projects = [];

/**
 * Collapses all expanded description cells back to their truncated state.
 */
function collapseAllDescriptions() {
    const expandedCells = document.querySelectorAll('.description-cell.expanded');
    expandedCells.forEach(cell => {
        cell.classList.remove('expanded');
        const span = cell.querySelector('.description-text');
        if (span && cell.dataset.short !== undefined) {
            span.textContent = cell.dataset.short;
        }
    });
}

/**
 * Renders the list of projects in the dedicated projects list card, if present.
 * Adds edit and delete buttons for each project.
 */
function renderProjectsList() {
    const projectsList = document.getElementById('projects-list');
    if (!projectsList) {
        return;
    }

    projectsList.innerHTML = '';

    if (!projects || projects.length === 0) {
        const li = document.createElement('li');
        li.textContent = 'No projects yet';
        projectsList.appendChild(li);
        return;
    }

    projects.forEach(project => {
        const li = document.createElement('li');

        const main = document.createElement('div');
        main.classList.add('project-main');

        const nameSpan = document.createElement('span');
        nameSpan.textContent = project.name;
        main.appendChild(nameSpan);

        if (project.description) {
            const descSpan = document.createElement('span');
            descSpan.textContent = ` — ${project.description}`;
            descSpan.classList.add('project-description');
            main.appendChild(descSpan);
        }

        if (project.color) {
            const colorDot = document.createElement('span');
            colorDot.textContent = '●';
            colorDot.classList.add('project-color-dot');
            colorDot.style.color = project.color;
            main.appendChild(colorDot);
        }

        const actions = document.createElement('div');
        actions.classList.add('project-actions');

        const editBtn = document.createElement('button');
        editBtn.type = 'button';
        editBtn.className = 'btn btn-secondary btn-sm';
        editBtn.textContent = 'Edit';
        editBtn.addEventListener('click', () => startEditProject(project));

        const deleteBtn = document.createElement('button');
        deleteBtn.type = 'button';
        deleteBtn.className = 'btn btn-danger btn-sm';
        deleteBtn.textContent = 'Delete';
        deleteBtn.addEventListener('click', async () => {
            await deleteProject(project.id);
        });

        actions.appendChild(editBtn);
        actions.appendChild(deleteBtn);

        li.appendChild(main);
        li.appendChild(actions);
        projectsList.appendChild(li);
    });
}

/**
 * Loads all projects from the API and populates the project dropdowns and project list.
 */
async function loadProjects() {
    const res = await fetch(PROJECTS_API);
    if (!res.ok) {
        console.error('Failed to load projects', res.status);
        return;
    }

    projects = await res.json();

    const projectSelect = document.getElementById('project');
    const filterProjectSelect = document.getElementById('filter-project');

    if (projectSelect) {
        projectSelect.innerHTML = '';
        const noneOption = document.createElement('option');
        noneOption.value = '';
        noneOption.textContent = 'No project';
        projectSelect.appendChild(noneOption);

        projects.forEach(project => {
            const opt = document.createElement('option');
            opt.value = project.id;
            opt.textContent = project.name;
            projectSelect.appendChild(opt);
        });
    }

    if (filterProjectSelect) {
        filterProjectSelect.innerHTML = '';
        const allOption = document.createElement('option');
        allOption.value = '';
        allOption.textContent = 'All projects';
        filterProjectSelect.appendChild(allOption);

        projects.forEach(project => {
            const opt = document.createElement('option');
            opt.value = project.id;
            opt.textContent = project.name;
            filterProjectSelect.appendChild(opt);
        });
    }
    renderProjectsList();
}

/**
 * Loads tasks from the API using current filter and sorting settings
 * and renders them into the table body.
 */
async function loadTasks(page = 0) {
    // Read filter and sort values from UI (if present)
    const statusFilter = document.getElementById('filter-status')?.value || '';
    const priorityFilter = document.getElementById('filter-priority')?.value || '';
    const projectFilter = document.getElementById('filter-project')?.value || '';
    const sortBy = document.getElementById('sort-by')?.value || 'createdAt';
    const sortDir = document.getElementById('sort-dir')?.value || 'desc';

    const params = new URLSearchParams();
    params.append('page', page.toString());
    params.append('size', '50');
    params.append('sort', `${sortBy},${sortDir}`);

    if (statusFilter) {
        params.append('status', statusFilter);
    }
    if (priorityFilter) {
        params.append('priority', priorityFilter);
    }
    if (projectFilter) {
        params.append('projectId', projectFilter);
    }

    const res = await fetch(`${API_BASE}?${params.toString()}`);
    if (!res.ok) {
        console.error('Failed to load tasks', res.status);
        alert('Failed to load tasks');
        return;
    }

    const pageData = await res.json();
    const tasks = pageData.content ?? pageData;

    const tbody = document.querySelector('#tasks-table tbody');
    tbody.innerHTML = '';
    collapseAllDescriptions();

    tasks.forEach(task => {
        const tr = document.createElement('tr');

        const status = task.status ?? 'OPEN';
        const priority = task.priority ?? 'MEDIUM';
        const statusClass = status.toLowerCase();
        const priorityClass = priority.toLowerCase();

        const statusBadge =
            `<span class="badge badge-status-${statusClass}">${status}</span>`;
        const priorityBadge =
            `<span class="badge badge-priority-${priorityClass}">${priority}</span>`;

        tr.innerHTML = `
            <td>${task.id}</td>
            <td>${task.title}</td>
            <td class="description-cell">
                <span class="description-text"></span>
            </td>
            <td>${statusBadge}</td>
            <td>${priorityBadge}</td>
            <td>${task.projectName || ''}</td>
            <td>${task.dueDate ?? ''}</td>
            <td class="actions-cell">
                <button data-id="${task.id}" class="btn btn-secondary btn-sm edit-btn">Edit</button>
                <button data-id="${task.id}" class="btn btn-danger btn-sm delete-btn">Delete</button>
            </td>
        `;

        const tbodyRef = document.querySelector('#tasks-table tbody');
        tbodyRef.appendChild(tr);

        // Description configuration
        const descCell = tr.querySelector('.description-cell');
        const descSpan = descCell.querySelector('.description-text');
        const full = task.description || '';
        const short = full.length > 60 ? full.substring(0, 60) + '…' : full;

        descCell.dataset.full = full;
        descCell.dataset.short = short;
        descSpan.textContent = short;

        descCell.addEventListener('click', (event) => {
            event.stopPropagation();
            const isExpanded = descCell.classList.contains('expanded');
            collapseAllDescriptions();
            if (!isExpanded) {
                descCell.classList.add('expanded');
                descSpan.textContent = descCell.dataset.full;
            }
        });

        // Edit-Button
        const editBtn = tr.querySelector('.edit-btn');
        editBtn.addEventListener('click', () => startEditTask(task));

        // Delete-Button
        const deleteBtn = tr.querySelector('.delete-btn');
        deleteBtn.addEventListener('click', async () => {
            await deleteTask(task.id);
            await loadTasks();
        });
    });
}

function startEditTask(task) {
    editingTaskId = task.id;

    document.getElementById('title').value = task.title ?? '';
    document.getElementById('description').value = task.description ?? '';
    document.getElementById('priority').value = task.priority ?? 'MEDIUM';
    document.getElementById('status').value = task.status ?? 'OPEN';
    document.getElementById('dueDate').value = task.dueDate ?? '';

    const projectSelect = document.getElementById('project');
    if (projectSelect) {
        projectSelect.value = task.projectId != null ? String(task.projectId) : '';
    }

    document.getElementById('submit-btn').textContent = 'Update task';
    document.getElementById('form-title').textContent = 'Update Task';

    document.getElementById('cancel-edit-btn').style.display = 'inline-flex';
    document.getElementById('task-card').classList.add('edit-mode');
}

function resetForm() {
    editingTaskId = null;
    document.getElementById('task-form').reset();
    document.getElementById('priority').value = 'MEDIUM';
    document.getElementById('status').value = 'OPEN';

    const projectSelect = document.getElementById('project');
    if (projectSelect) {
        projectSelect.value = '';
    }

    document.getElementById('submit-btn').textContent = 'Create task';
    document.getElementById('form-title').textContent = 'Create Task';

    document.getElementById('cancel-edit-btn').style.display = 'none';
    document.getElementById('task-card').classList.remove('edit-mode');
}

async function handleSubmit(event) {
    event.preventDefault();

    const title = document.getElementById('title').value.trim();
    const description = document.getElementById('description').value.trim();
    const priority = document.getElementById('priority').value;
    const status = document.getElementById('status').value;
    const dueDate = document.getElementById('dueDate').value || null;
    const projectSelect = document.getElementById('project');
    const projectIdValue = projectSelect ? projectSelect.value : '';
    const projectId = projectIdValue ? Number(projectIdValue) : null;

    if (!title) {
        alert('Title is required');
        return;
    }

    const body = {
        title,
        description,
        priority,
        status,
        dueDate,
        projectId
    };

    const url = editingTaskId ? `${API_BASE}/${editingTaskId}` : API_BASE;
    const method = editingTaskId ? 'PUT' : 'POST';

    const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });

    if (!res.ok) {
        const error = await res.json().catch(() => ({}));
        console.error('Failed to save task', res.status, error);
        alert('Failed to save task');
        return;
    }

    resetForm();
    await loadTasks();
}

/**
 * Starts editing of a selected project in the project form.
 */
function startEditProject(project) {
    editingProjectId = project.id;

    document.getElementById('project-name').value = project.name ?? '';
    document.getElementById('project-description').value = project.description ?? '';
    document.getElementById('project-color').value = project.color ?? '';

    document.getElementById('project-submit-btn').textContent = 'Update project';
    document.getElementById('project-form-title').textContent = 'Update project';
    document.getElementById('project-cancel-btn').style.display = 'inline-flex';
    document.getElementById('projects-card').classList.add('edit-mode');
}

/**
 * Resets the project form back to create mode.
 */
function resetProjectForm() {
    editingProjectId = null;

    document.getElementById('project-form').reset();
    document.getElementById('project-submit-btn').textContent = 'Create project';
    document.getElementById('project-form-title').textContent = 'Create project';
    document.getElementById('project-cancel-btn').style.display = 'none';
    document.getElementById('projects-card').classList.remove('edit-mode');
}

/**
 * Handles creation or update of a project via the project form.
 */
async function handleProjectSubmit(event) {
    event.preventDefault();

    const nameInput = document.getElementById('project-name');
    const descInput = document.getElementById('project-description');
    const colorInput = document.getElementById('project-color');

    const name = nameInput.value.trim();
    const description = descInput.value.trim();
    const color = colorInput.value.trim();

    if (!name) {
        alert('Project name is required');
        return;
    }

    const body = {
        name,
        description: description || null,
        color: color || null
    };

    const url = editingProjectId
        ? `${PROJECTS_API}/${editingProjectId}`
        : PROJECTS_API;
    const method = editingProjectId ? 'PUT' : 'POST';

    const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });

    if (!res.ok) {
        const error = await res.json().catch(() => ({}));
        console.error('Failed to save project', res.status, error);
        alert('Failed to save project');
        return;
    }

    resetProjectForm();
    await loadProjects();
    await loadTasks();
}

/**
 * Deletes a project by id after user confirmation.
 */
async function deleteProject(id) {
    const confirmDelete = confirm('Delete this project? Tasks may be affected.');
    if (!confirmDelete) {
        return;
    }

    const res = await fetch(`${PROJECTS_API}/${id}`, { method: 'DELETE' });

    if (!res.ok && res.status !== 204) {
        console.error('Failed to delete project', res.status);
        alert('Failed to delete project');
        return;
    }

    if (editingProjectId === id) {
        resetProjectForm();
    }

    await loadProjects();
    await loadTasks();
}

async function deleteTask(id) {
    const res = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
    if (!res.ok && res.status !== 204) {
        console.error('Failed to delete task', res.status);
        alert('Failed to delete task');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('task-form').addEventListener('submit', handleSubmit);

    const refreshBtn = document.getElementById('refresh-btn');
    if (refreshBtn) {
        refreshBtn.addEventListener('click', () => loadTasks());
    }

    const cancelBtn = document.getElementById('cancel-edit-btn');
    cancelBtn.addEventListener('click', resetForm);

    // Project form & refresh button
    const projectForm = document.getElementById('project-form');
    if (projectForm) {
        projectForm.addEventListener('submit', handleProjectSubmit);
    }

    const projectCancelBtn = document.getElementById('project-cancel-btn');
    if (projectCancelBtn) {
        projectCancelBtn.addEventListener('click', resetProjectForm);
    }

    const projectsRefreshBtn = document.getElementById('projects-refresh-btn');
    if (projectsRefreshBtn) {
        projectsRefreshBtn.addEventListener('click', () => loadProjects());
    }

    // Collapse descriptions when clicking outside
    document.addEventListener('click', (event) => {
        if (!event.target.closest('.description-cell')) {
            collapseAllDescriptions();
        }
    });

    // Filter & Sort Event-Handler
    const statusFilter = document.getElementById('filter-status');
    const priorityFilter = document.getElementById('filter-priority');
    const projectFilter = document.getElementById('filter-project');
    const sortBySelect = document.getElementById('sort-by');
    const sortDirSelect = document.getElementById('sort-dir');

    if (statusFilter) {
        statusFilter.addEventListener('change', () => loadTasks());
    }
    if (priorityFilter) {
        priorityFilter.addEventListener('change', () => loadTasks());
    }
    if (projectFilter) {
        projectFilter.addEventListener('change', () => loadTasks());
    }
    if (sortBySelect) {
        sortBySelect.addEventListener('change', () => loadTasks());
    }
    if (sortDirSelect) {
        sortDirSelect.addEventListener('change', () => loadTasks());
    }

    // First load projects, then tasks
    loadProjects().then(() => loadTasks());
});