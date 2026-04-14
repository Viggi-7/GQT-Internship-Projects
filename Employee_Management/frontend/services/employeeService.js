const BASE = 'http://localhost:8080/api/employees';

export async function fetchEmployees({page=0,size=5,search=''}) {
  const params = new URLSearchParams();
  params.set('page', page);
  params.set('size', size);
  if (search) params.set('search', search);
  const res = await fetch(`${BASE}?${params.toString()}`);
  if (!res.ok) throw new Error('Failed to fetch employees');
  return res.json(); // Spring Page JSON
}

export async function createEmployee(emp) {
  const res = await fetch(BASE, {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify(emp)
  });
  if (!res.ok) throw new Error('Create failed');
  return res.json();
}

export async function updateEmployee(id, emp) {
  const res = await fetch(`${BASE}/${id}`, {
    method: 'PUT',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify(emp)
  });
  if (!res.ok) throw new Error('Update failed');
  return res.json();
}

export async function deleteEmployee(id) {
  const res = await fetch(`${BASE}/${id}`, { method: 'DELETE' });
  if (!res.ok && res.status !== 204) throw new Error('Delete failed');
  return true;
}
