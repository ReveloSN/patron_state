import type { ReactionRecipe } from "../../types/catalog";

interface ReactionRecipeCatalogPanelProps {
  recipes: ReactionRecipe[];
  pending: boolean;
  error: string | null;
}

export function ReactionRecipeCatalogPanel({
  recipes,
  pending,
  error,
}: ReactionRecipeCatalogPanelProps) {
  return (
    <section className="panel-section">
      <div className="section-heading">
        <h3>Educational recipes</h3>
        <p>
          The local recipe catalog is always available, so the system remains useful even without
          internet access.
        </p>
      </div>

      {error && <div className="banner">{error}</div>}

      {pending ? (
        <p className="empty-block">Loading local educational recipes...</p>
      ) : (
        <div className="recipe-grid">
          {recipes.map((recipe) => (
            <article className="recipe-card" key={recipe.name}>
              <div className="recipe-card__header">
                <h4>{recipe.name}</h4>
                {recipe.catalyst && <span className="pill pill--neutral">Catalyst</span>}
              </div>

              <p>{recipe.summary}</p>

              <div className="detail-group">
                <strong>Reactants</strong>
                <ul className="detail-list">
                  {recipe.reactants.map((reactant) => (
                    <li key={reactant}>{reactant}</li>
                  ))}
                </ul>
              </div>

              <div className="detail-group">
                <strong>Products</strong>
                <ul className="detail-list">
                  {recipe.products.map((product) => (
                    <li key={product}>{product}</li>
                  ))}
                </ul>
              </div>

              <div className="detail-group">
                <strong>Catalyst</strong>
                <p className="muted-copy">{recipe.catalyst ?? "No catalyst required"}</p>
              </div>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}
