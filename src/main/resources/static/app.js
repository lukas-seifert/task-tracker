const API_BASE = '/api/tasks';
let editingTaskId = null;

async function loadTasks() {
    const res = await fetch(API_BASE);
    if (!res.ok) {
        console.error('Failed to load tasks', res.status);
        alert('Failed to load tasks');
        return;
    }

    const page = await res.json();
    const tasks = page.content ?? page; // works with or without pagination

    const tbody = document.querySelector('#tasks-table tbody');
    tbody.innerHTML = '';

    tasks.forEach(task => {
        const tr = document.createElement('tr');

        const status = task.status ?? 'OPEN';
        const priority = task.priority ?? 'MEDIUM';
        const statusClass = status.toLowerCase();        // e.g. IN_PROGRESS -> in_progress
        const priorityClass = priority.toLowerCase();    // e.g. HIGH -> high

        tr.innerHTML = `
            <td>${task.id}</td>
            <td>${task.title}</td>
            <td>
                <span class="badge badge-status-${statusClass}">
                    ${status}
                </span>
            </td>
            <td>
                <span class="badge badge-priority-${priorityClass}">
                    ${priority}
                </span>
            </td>
            <td>${task.dueDate ?? ''}</td>
            <td class="actions-cell">
                <button data-id="${task.id}" class="btn btn-secondary btn-sm edit-btn">Edit</button>
                <button data-id="${task.id}" class="btn btn-danger btn-sm delete-btn">Delete</button>
            </td>
        `;

        tbody.appendChild(tr);

        const editBtn = tr.querySelector('.edit-btn');
        editBtn.addEventListener('click', () => startEditTask(task));

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
        refreshBtn.addEventListener('click', loadTasks);
    }

    const cancelBtn = document.getElementById('cancel-edit-btn');
    cancelBtn.addEventListener('click', resetForm);

    loadTasks();
});