require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/gwt'

desc 'GWT EventSource Library'
define 'gwt-eventsource' do
  project.group = 'org.realityforge.gwt.eventsource'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/gwt-eventsource')
  pom.add_developer('realityforge', 'Peter Donald')
  pom.provided_dependencies.concat [:javax_annotation, :gwt_user]

  compile.with :javax_annotation, :gwt_user

  test.using :testng
  test.with :mockito

  package(:jar).include("#{_(:source, :main, :java)}/*")
  package(:sources)
  package(:javadoc)
end
