const API_BASE = '/api/tasks';
let editingTaskId = null;

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
 * Loads tasks from the API with current filter and sorting settings
 * and renders them into the table body.
 */
async function loadTasks(page = 0) {
    // Read filter and sort values from UI (if present)
    const statusFilter = document.getElementById('filter-status')?.value || '';
    const priorityFilter = document.getElementById('filter-priority')?.value || '';
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

    if (!title) {
        alert('Title is required');
        return;
    }

    const body = {
        title,
        description,
        priority,
        status,
        dueDate
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

    // Collapse descriptions when clicking outside
    document.addEventListener('click', (event) => {
        if (!event.target.closest('.description-cell')) {
            collapseAllDescriptions();
        }
    });

    // Filter & Sort Event-Handler
    const statusFilter = document.getElementById('filter-status');
    const priorityFilter = document.getElementById('filter-priority');
    const sortBySelect = document.getElementById('sort-by');
    const sortDirSelect = document.getElementById('sort-dir');

    if (statusFilter) {
        statusFilter.addEventListener('change', () => loadTasks());
    }
    if (priorityFilter) {
        priorityFilter.addEventListener('change', () => loadTasks());
    }
    if (sortBySelect) {
        sortBySelect.addEventListener('change', () => loadTasks());
    }
    if (sortDirSelect) {
        sortDirSelect.addEventListener('change', () => loadTasks());
    }

    // Initial load
    loadTasks();
});