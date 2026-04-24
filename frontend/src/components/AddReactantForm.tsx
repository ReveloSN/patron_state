import { useState, type FormEvent } from "react";
import type { AddReactantPayload } from "../types/api";
import type { Reactant } from "../types/reaction";

interface AddReactantFormProps {
  reactants: Reactant[];
  enabled: boolean;
  pending: boolean;
  onSubmit: (payload: AddReactantPayload) => Promise<void>;
}

export function AddReactantForm({
  reactants,
  enabled,
  pending,
  onSubmit,
}: AddReactantFormProps) {
  const [name, setName] = useState("");
  const [quantity, setQuantity] = useState("");
  const [unit, setUnit] = useState("mL");

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    await onSubmit({
      name,
      quantity: Number(quantity),
      unit,
    });

    setName("");
    setQuantity("");
    setUnit("mL");
  }

  return (
    <section className="panel-section">
      <div className="section-heading">
        <h3>Add reactants</h3>
        <p>Reactants can only be registered while the reaction is still in the prepared phase.</p>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="field field--full">
            <label htmlFor="reactant-name">Reactant name</label>
            <input
              id="reactant-name"
              value={name}
              onChange={(event) => setName(event.target.value)}
              placeholder="Acetic Acid"
              required
              disabled={!enabled || pending}
            />
          </div>

          <div className="field">
            <label htmlFor="reactant-quantity">Quantity</label>
            <input
              id="reactant-quantity"
              type="number"
              step="0.1"
              min="0"
              value={quantity}
              onChange={(event) => setQuantity(event.target.value)}
              placeholder="25"
              required
              disabled={!enabled || pending}
            />
          </div>

          <div className="field">
            <label htmlFor="reactant-unit">Unit</label>
            <input
              id="reactant-unit"
              value={unit}
              onChange={(event) => setUnit(event.target.value)}
              placeholder="mL"
              required
              disabled={!enabled || pending}
            />
          </div>
        </div>

        <div className="action-row">
          <button className="button button--primary" type="submit" disabled={!enabled || pending}>
            {pending ? "Saving..." : "Add reactant"}
          </button>
        </div>
      </form>

      {reactants.length === 0 ? (
        <p className="empty-block">No reactants registered yet.</p>
      ) : (
        <ul className="reactant-list">
          {reactants.map((reactant) => (
            <li key={`${reactant.name}-${reactant.unit}-${reactant.quantity}`}>
              <strong>{reactant.name}</strong>
              <span>
                {reactant.quantity} {reactant.unit}
              </span>
            </li>
          ))}
        </ul>
      )}
    </section>
  );
}
