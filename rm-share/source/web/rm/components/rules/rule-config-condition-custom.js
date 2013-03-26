/**
 * RuleConfigConditionCustom.
 *
 * @namespace Alfresco
 * @class Alfresco.RuleConfigConditionCustom
 */
(function()
{
   Alfresco.RuleConfigConditionCustom = function(htmlId)
   {
      Alfresco.RuleConfigActionCustom.superclass.constructor.call(this, htmlId);

      // Re-register with our own name
      this.name = "Alfresco.RuleConfigConditionCustom";
      Alfresco.util.ComponentManager.reregister(this);

      // Instance variables
      this.customisations = YAHOO.lang.merge(this.customisations, Alfresco.RuleConfigConditionCustom.superclass.customisations);
      this.renderers = YAHOO.lang.merge(this.renderers, Alfresco.RuleConfigConditionCustom.superclass.renderers);

      return this;
   };

   YAHOO.extend(Alfresco.RuleConfigConditionCustom, Alfresco.RuleConfigCondition,
   {
      customisations:
      {

      },
      renderers:
      {
        
      }
   });
 })();