import React, {useState, useEffect} from 'react';

export default function EmployeeForm({initial, onCancel, onSave}) {
  const [form, setForm] = useState({
    firstName: '', lastName: '', email: '', department: '', salary: ''
  });

  useEffect(()=> { if (initial) setForm({...initial, salary: initial.salary ?? ''}); }, [initial]);

  function change(e) {
    const {name, value} = e.target;
    setForm(prev => ({...prev, [name]: value}));
  }

  function submit(e){
    e.preventDefault();
    // basic client validation
    if (!form.firstName || !form.lastName || !form.email) {
      alert('First name, last name and email required');
      return;
    }
    onSave({...form, salary: form.salary ? Number(form.salary) : null});
    setForm({firstName:'', lastName:'', email:'', department:'', salary:''});
  }

  return (
    <form className="emp-form" onSubmit={submit}>
      <input name="firstName" placeholder="First name" value={form.firstName} onChange={change} />
      <input name="lastName" placeholder="Last name" value={form.lastName} onChange={change} />
      <input name="email" placeholder="email" value={form.email} onChange={change} />
      <input name="department" placeholder="department" value={form.department} onChange={change} />
      <input name="salary" placeholder="salary" value={form.salary} onChange={change} />
      <div>
        <button type="submit">Save</button>
        <button type="button" onClick={onCancel}>Cancel</button>
      </div>
    </form>
  );
}
