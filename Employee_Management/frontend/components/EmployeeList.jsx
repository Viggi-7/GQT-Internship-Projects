import React, {useEffect, useState} from 'react';
import { fetchEmployees, createEmployee, updateEmployee, deleteEmployee } from '../services/employeeService';
import EmployeeForm from './EmployeeForm';

export default function EmployeeList(){
  const [data, setData] = useState({content:[], totalPages:0, number:0});
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [size] = useState(5);
  const [search, setSearch] = useState('');
  const [editing, setEditing] = useState(null);
  const [showForm, setShowForm] = useState(false);

  async function load(p=0, q='') {
    setLoading(true);
    try {
      const res = await fetchEmployees({page:p, size, search: q});
      setData(res);
    } catch (e) {
      console.error(e); alert('Failed to load employees');
    } finally { setLoading(false); }
  }

  useEffect(()=> { load(page, search); }, [page, search]);

  function handleAdd(){
    setEditing(null);
    setShowForm(true);
  }

  async function handleSave(emp){
    try {
      if (editing) {
        await updateEmployee(editing.id, emp);
      } else {
        await createEmployee(emp);
      }
      setShowForm(false);
      load(0, search);
      setPage(0);
    } catch (e) { console.error(e); alert('Save failed'); }
  }

  function handleEdit(emp){
    setEditing(emp);
    setShowForm(true);
  }

  async function handleDelete(id){
    if (!confirm('Delete this employee?')) return;
    try {
      await deleteEmployee(id);
      load(page, search);
    } catch (e) { console.error(e); alert('Delete failed'); }
  }

  return (
    <div>
      <div className="toolbar">
        <input placeholder="Search name or email..." value={search} onChange={e=>setSearch(e.target.value)} />
        <button onClick={()=>{ setSearch(''); load(0,''); }}>Clear</button>
        <button onClick={handleAdd}>Add Employee</button>
      </div>

      {showForm && <EmployeeForm initial={editing} onSave={handleSave} onCancel={()=>setShowForm(false)} />}

      {loading ? <p>Loading...</p> :
      <table className="emp-table">
        <thead><tr><th>First</th><th>Last</th><th>Email</th><th>Dept</th><th>Salary</th><th>Actions</th></tr></thead>
        <tbody>
          {data.content.map(emp => (
            <tr key={emp.id}>
              <td>{emp.firstName}</td>
              <td>{emp.lastName}</td>
              <td>{emp.email}</td>
              <td>{emp.department}</td>
              <td>{emp.salary ?? '-'}</td>
              <td>
                <button onClick={()=>handleEdit(emp)}>Edit</button>
                <button onClick={()=>handleDelete(emp.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      }

      <div className="pager">
        <button onClick={()=>setPage(p=>Math.max(0,p-1))} disabled={data.number===0}>Prev</button>
        <span> Page {data.number+1} of {data.totalPages} </span>
        <button onClick={()=>setPage(p=>Math.min(data.totalPages-1, p+1))} disabled={data.number+1>=data.totalPages}>Next</button>
      </div>
    </div>
  );
}
